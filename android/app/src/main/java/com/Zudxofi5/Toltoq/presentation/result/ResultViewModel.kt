package com.Zudxofi5.Toltoq.presentation.result

import androidx.lifecycle.ViewModel
import com.Zudxofi5.Toltoq.domain.repository.LevelRepository
import com.Zudxofi5.Toltoq.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ResultViewModel(
    private val progress: ProgressRepository,
    private val levels: LevelRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ResultUiState())
    val state: StateFlow<ResultUiState> = _state.asStateFlow()

    fun bind(levelId: Int) {
        val catalogue = levels.levels()
        val index = catalogue.indexOfFirst { it.id == levelId }
        val next = if (index >= 0 && index + 1 < catalogue.size) catalogue[index + 1] else null
        val unlocked = next != null && progress.levelProgress(next.id).unlocked
        _state.value = ResultUiState(
            bestScore = progress.levelProgress(levelId).bestScore,
            nextLevelId = next?.id ?: 0,
            hasNextLevel = unlocked
        )
    }
}
