package com.Zudxofi5.Toltoq.presentation.game

import com.Zudxofi5.Toltoq.domain.model.Cell
import com.Zudxofi5.Toltoq.domain.model.CellKind
import com.Zudxofi5.Toltoq.domain.model.Usher

enum class GamePhase { REHEARSAL, PLAYING, FINISHED }

enum class FlashKind { NONE, SPOTTED, NEAR_MISS }

data class GameUiState(
    val levelId: Int = 1,
    val act: Int = 1,
    val roomNameRes: Int = 0,
    val cols: Int = 6,
    val rows: Int = 8,
    val kinds: List<CellKind> = emptyList(),
    val joker: Cell = Cell(0, 0),
    val door: Cell = Cell(0, 0),
    val pieces: Set<Cell> = emptySet(),
    val ushers: List<Usher> = emptyList(),
    val ghosts: List<Usher> = emptyList(),
    val coneCells: Set<Cell> = emptySet(),
    val legalMoves: Set<Cell> = emptySet(),
    val movesLeft: Int = 0,
    val budget: Int = 0,
    val piecesHeld: Int = 0,
    val piecesTarget: Int = 3,
    val bellsLeft: Int = 0,
    val spotlightCol: Int = -1,
    val hidden: Boolean = false,
    val masqueradeReady: Boolean = false,
    val phase: GamePhase = GamePhase.REHEARSAL,
    val rehearsalSecondsLeft: Int = 0,
    val paused: Boolean = false,
    val confirmRestart: Boolean = false,
    val flash: FlashKind = FlashKind.NONE,
    val showControlHint: Boolean = true,
    val conesVisible: Boolean = true,
    val moveHints: Boolean = true,
    val highContrast: Boolean = false,
    val reducedMotion: Boolean = false,
    val goldTrim: Boolean = false
)
