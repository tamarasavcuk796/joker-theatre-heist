package com.Zudxofi5.Toltoq.domain.repository

import com.Zudxofi5.Toltoq.domain.model.GameSettings

interface SettingsRepository {
    fun load(): GameSettings
    fun save(settings: GameSettings)
}
