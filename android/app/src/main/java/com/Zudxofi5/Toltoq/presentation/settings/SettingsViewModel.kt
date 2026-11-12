package com.Zudxofi5.Toltoq.presentation.settings

import androidx.lifecycle.ViewModel
import com.Zudxofi5.Toltoq.domain.model.GameSettings
import com.Zudxofi5.Toltoq.domain.repository.ProgressRepository
import com.Zudxofi5.Toltoq.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel(
    private val settings: SettingsRepository,
    private val progress: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        _state.value = SettingsUiState(settings = settings.load())
    }

    fun setRehearsalIndex(index: Int) {
        val seconds = REHEARSAL_OPTIONS.getOrElse(index) { 5 }
        mutate { it.copy(rehearsalSeconds = seconds) }
    }

    fun setConeMode(index: Int) {
        mutate { it.copy(conesAlwaysVisible = index == 1) }
    }

    fun setHighContrast(value: Boolean) {
        mutate { it.copy(highContrast = value) }
    }

    fun setReducedMotion(value: Boolean) {
        mutate { it.copy(reducedMotion = value) }
    }

    fun setMoveHints(value: Boolean) {
        mutate { it.copy(moveHints = value) }
    }

    fun askReset() {
        _state.update { it.copy(confirmReset = true) }
    }

    fun dismissReset() {
        _state.update { it.copy(confirmReset = false) }
    }

    fun confirmReset() {
        progress.resetAll()
        _state.update { it.copy(confirmReset = false) }
    }

    private fun mutate(transform: (GameSettings) -> GameSettings) {
        val updated = transform(_state.value.settings)
        settings.save(updated)
        _state.update { it.copy(settings = updated) }
    }

    companion object {
        val REHEARSAL_OPTIONS = listOf(3, 5, 8)
    }
}
