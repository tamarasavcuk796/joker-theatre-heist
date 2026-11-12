package com.Zudxofi5.Toltoq.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Zudxofi5.Toltoq.core.config.GameConfig
import com.Zudxofi5.Toltoq.domain.model.Cell
import com.Zudxofi5.Toltoq.domain.model.CellKind
import com.Zudxofi5.Toltoq.domain.model.GameSettings
import com.Zudxofi5.Toltoq.domain.model.Level
import com.Zudxofi5.Toltoq.domain.model.RunOutcome
import com.Zudxofi5.Toltoq.domain.model.RunResult
import com.Zudxofi5.Toltoq.domain.model.RunState
import com.Zudxofi5.Toltoq.domain.model.Usher
import com.Zudxofi5.Toltoq.domain.model.visionCells
import com.Zudxofi5.Toltoq.domain.repository.LevelRepository
import com.Zudxofi5.Toltoq.domain.repository.ProgressRepository
import com.Zudxofi5.Toltoq.domain.repository.SettingsRepository
import com.Zudxofi5.Toltoq.domain.usecase.AdvanceTurnUseCase
import com.Zudxofi5.Toltoq.domain.usecase.ResolveDetectionUseCase
import com.Zudxofi5.Toltoq.domain.usecase.ScoreRunUseCase
import com.Zudxofi5.Toltoq.domain.usecase.ThrowBellUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class GameViewModel(
    levelId: Int,
    levels: LevelRepository,
    private val progress: ProgressRepository,
    private val settings: SettingsRepository
) : ViewModel() {

    private val advanceTurn = AdvanceTurnUseCase()
    private val resolveDetection = ResolveDetectionUseCase()
    private val throwBell = ThrowBellUseCase()
    private val scoreRun = ScoreRunUseCase()

    private val level: Level = levels.level(levelId)
    private val options: GameSettings = settings.load()
    private val goldTrim: Boolean = progress.equippedCostumeId() >= 0

    private var run: RunState = RunState.opening(level)
    private var usedBell = false
    private var closed = false
    private var rehearsal: Job? = null
    private var flashJob: Job? = null

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val _events = Channel<RunResult>(Channel.BUFFERED)
    val events: Flow<RunResult> = _events.receiveAsFlow()

    init {
        beginLevel()
    }

    fun onTile(cell: Cell) {
        val snapshot = _state.value
        if (snapshot.phase != GamePhase.PLAYING || snapshot.paused || closed) return
        if (!snapshot.legalMoves.contains(cell)) return
        takeTurn(cell)
    }

    fun onWait() {
        val snapshot = _state.value
        if (snapshot.phase != GamePhase.PLAYING || snapshot.paused || closed) return
        takeTurn(run.joker)
    }

    fun onBell() {
        val snapshot = _state.value
        if (snapshot.phase != GamePhase.PLAYING || snapshot.paused || closed) return
        if (run.bellsLeft <= 0) return
        usedBell = true
        run = throwBell(run, GameConfig.BELL_RANGE, GameConfig.LURE_TURNS)
        takeTurn(run.joker)
    }

    fun onPause() {
        if (closed) return
        _state.update { it.copy(paused = true) }
    }

    fun onResume() {
        _state.update { it.copy(paused = false, confirmRestart = false) }
    }

    fun onAskRestart() {
        _state.update { it.copy(confirmRestart = true) }
    }

    fun onDismissRestart() {
        _state.update { it.copy(confirmRestart = false) }
    }

    fun onRestart() {
        rehearsal?.cancel()
        flashJob?.cancel()
        closed = false
        usedBell = false
        beginLevel()
    }

    fun stop() {
        rehearsal?.cancel()
        flashJob?.cancel()
    }

    private fun beginLevel() {
        run = RunState.opening(level)
        _state.value = compose(GamePhase.REHEARSAL, options.rehearsalSeconds, emptyList())
        rehearsal?.cancel()
        rehearsal = viewModelScope.launch {
            var ghosts: List<Usher> = level.ushers
            var ticks = options.rehearsalSeconds * 4
            while (isActive && ticks > 0) {
                delay(250)
                if (_state.value.paused) continue
                ghosts = ghosts.map { it.step() }
                ticks -= 1
                val secondsLeft = (ticks + 3) / 4
                val cones = LinkedHashSet<Cell>()
                for (ghost in ghosts) {
                    cones.addAll(visionCells(ghost, level, GameConfig.VISION_RANGE))
                }
                _state.update {
                    it.copy(
                        ghosts = ghosts,
                        coneCells = cones,
                        rehearsalSecondsLeft = secondsLeft
                    )
                }
            }
            _state.update {
                it.copy(phase = GamePhase.PLAYING, ghosts = emptyList(), rehearsalSecondsLeft = 0)
            }
            delay(4000)
            _state.update { it.copy(showControlHint = false) }
        }
    }

    private fun takeTurn(target: Cell) {
        val previous = run.joker
        var pieces = run.pieces
        var held = run.piecesHeld
        if (pieces.contains(target)) {
            pieces = pieces - target
            held += 1
        }
        run = run.copy(
            joker = target,
            lastCell = previous,
            pieces = pieces,
            piecesHeld = held,
            movesLeft = run.movesLeft - 1,
            masqueradeReady = run.masqueradeReady || held >= GameConfig.PIECES_PER_LEVEL
        )
        if (target == level.door) {
            publish(FlashKind.NONE)
            closeRound(RunOutcome.ESCAPED)
            return
        }
        run = advanceTurn(level, run)
        val caught = resolveDetection(level, run, GameConfig.VISION_RANGE)
        if (caught && run.masqueradeReady) {
            run = run.copy(masqueradeReady = false)
            publish(FlashKind.NEAR_MISS)
            clearFlashLater()
            return
        }
        if (caught) {
            publish(FlashKind.SPOTTED)
            closeRound(RunOutcome.SPOTTED)
            return
        }
        publish(FlashKind.NONE)
        if (run.movesLeft <= 0) {
            closeRound(RunOutcome.OUT_OF_MOVES)
        }
    }

    private fun clearFlashLater() {
        flashJob?.cancel()
        flashJob = viewModelScope.launch {
            delay(420)
            _state.update { it.copy(flash = FlashKind.NONE) }
        }
    }

    private fun closeRound(outcome: RunOutcome) {
        if (closed) return
        closed = true
        rehearsal?.cancel()
        _state.update { it.copy(phase = GamePhase.FINISHED) }
        viewModelScope.launch {
            delay(if (outcome == RunOutcome.ESCAPED) 620L else 880L)
            val scored = scoreRun(level, run, outcome, usedBell, GameConfig.PIECES_PER_LEVEL)
            val stored = progress.saveRun(scored)
            _events.send(stored)
        }
    }

    private fun publish(flash: FlashKind) {
        val phase = _state.value.phase
        _state.value = compose(phase, 0, emptyList()).copy(
            flash = flash,
            showControlHint = _state.value.showControlHint,
            paused = _state.value.paused
        )
    }

    private fun compose(
        phase: GamePhase,
        rehearsalSecondsLeft: Int,
        ghosts: List<Usher>
    ): GameUiState {
        val visibleCones = options.conesAlwaysVisible || phase == GamePhase.REHEARSAL
        val source = if (phase == GamePhase.REHEARSAL && ghosts.isNotEmpty()) ghosts else run.ushers
        val cones = LinkedHashSet<Cell>()
        for (usher in source) {
            cones.addAll(visionCells(usher, level, GameConfig.VISION_RANGE))
        }
        return GameUiState(
            levelId = level.id,
            act = level.act,
            roomNameRes = level.nameRes,
            cols = level.cols,
            rows = level.rows,
            kinds = level.kinds,
            joker = run.joker,
            door = level.door,
            pieces = run.pieces,
            ushers = run.ushers,
            ghosts = ghosts,
            coneCells = cones,
            legalMoves = if (phase == GamePhase.PLAYING) legalMoves() else emptySet(),
            movesLeft = run.movesLeft,
            budget = level.budget,
            piecesHeld = run.piecesHeld,
            piecesTarget = GameConfig.PIECES_PER_LEVEL,
            bellsLeft = run.bellsLeft,
            spotlightCol = run.spotlightCol,
            hidden = level.kindAt(run.joker) == CellKind.PROP,
            masqueradeReady = run.masqueradeReady,
            phase = phase,
            rehearsalSecondsLeft = rehearsalSecondsLeft,
            paused = false,
            confirmRestart = false,
            flash = FlashKind.NONE,
            showControlHint = true,
            conesVisible = visibleCones,
            moveHints = options.moveHints,
            highContrast = options.highContrast,
            reducedMotion = options.reducedMotion,
            goldTrim = goldTrim
        )
    }

    private fun legalMoves(): Set<Cell> {
        val origin = run.joker
        val candidates = listOf(
            Cell(origin.col + 1, origin.row),
            Cell(origin.col - 1, origin.row),
            Cell(origin.col, origin.row + 1),
            Cell(origin.col, origin.row - 1)
        )
        return candidates
            .filter { level.inBounds(it) && level.kindAt(it) != CellKind.WALL }
            .toSet()
    }
}
