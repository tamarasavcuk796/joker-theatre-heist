package com.Zudxofi5.Toltoq.presentation.progress

import androidx.lifecycle.ViewModel
import com.Zudxofi5.Toltoq.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProgressViewModel(private val progress: ProgressRepository) : ViewModel() {

    private val _state = MutableStateFlow(ProgressUiState())
    val state: StateFlow<ProgressUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        _state.value = ProgressUiState(
            career = progress.career(),
            acts = progress.acts()
        )
    }
}
