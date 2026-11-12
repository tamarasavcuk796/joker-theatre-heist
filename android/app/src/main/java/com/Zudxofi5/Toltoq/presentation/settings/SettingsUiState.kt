package com.Zudxofi5.Toltoq.presentation.settings

import com.Zudxofi5.Toltoq.domain.model.GameSettings

data class SettingsUiState(
    val settings: GameSettings = GameSettings(),
    val confirmReset: Boolean = false
)
