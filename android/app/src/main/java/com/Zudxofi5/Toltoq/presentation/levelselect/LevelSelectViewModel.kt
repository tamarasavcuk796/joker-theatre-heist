package com.Zudxofi5.Toltoq.presentation.levelselect

import androidx.lifecycle.ViewModel
import com.Zudxofi5.Toltoq.domain.repository.LevelRepository
import com.Zudxofi5.Toltoq.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LevelSelectViewModel(
    private val progress: ProgressRepository,
    private val levels: LevelRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LevelSelectUiState())
    val state: StateFlow<LevelSelectUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val stored = progress.allLevelProgress().associateBy { it.levelId }
        val plaques = levels.levels().map { level ->
            val entry = stored[level.id]
            LevelPlaque(
                levelId = level.id,
                act = level.act,
                nameRes = level.nameRes,
                stars = entry?.stars ?: 0,
                unlocked = entry?.unlocked ?: false
            )
        }
        val acts = progress.acts()
        val current = _state.value.selectedAct
        val fallback = acts.lastOrNull { it.unlocked }?.act ?: 1
        val selected = if (acts.any { it.act == current && it.unlocked }) current else fallback
        _state.update {
            it.copy(acts = acts, plaques = plaques, selectedAct = selected)
        }
    }

    fun selectAct(act: Int) {
        if (_state.value.acts.any { it.act == act && it.unlocked }) {
            _state.update { it.copy(selectedAct = act) }
        }
    }
}
