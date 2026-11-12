package com.Zudxofi5.Toltoq.presentation.menu

import androidx.lifecycle.ViewModel
import com.Zudxofi5.Toltoq.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MenuViewModel(private val progress: ProgressRepository) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val career = progress.career()
        val levels = progress.allLevelProgress()
        _state.value = MenuUiState(
            levelsCleared = career.levelsCleared,
            totalLevels = career.totalLevels,
            bestScore = levels.maxOfOrNull { it.bestScore } ?: 0,
            pieces = career.pieces,
            maxPieces = career.maxPieces,
            nextLevelId = progress.nextUnlockedLevelId()
        )
    }
}
