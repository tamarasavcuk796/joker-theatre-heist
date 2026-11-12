package com.Zudxofi5.Toltoq.data.repository

import com.Zudxofi5.Toltoq.data.local.PreferencesStorage
import com.Zudxofi5.Toltoq.domain.model.GameSettings
import com.Zudxofi5.Toltoq.domain.repository.SettingsRepository

class SettingsRepositoryImpl(private val storage: PreferencesStorage) : SettingsRepository {

    override fun load(): GameSettings = GameSettings(
        rehearsalSeconds = storage.readInt(KEY_REHEARSAL, 5),
        conesAlwaysVisible = storage.readFlag(KEY_CONES, false),
        highContrast = storage.readFlag(KEY_CONTRAST, false),
        reducedMotion = storage.readFlag(KEY_MOTION, false),
        moveHints = storage.readFlag(KEY_HINTS, true)
    )

    override fun save(settings: GameSettings) {
        storage.writeInt(KEY_REHEARSAL, settings.rehearsalSeconds)
        storage.writeFlag(KEY_CONES, settings.conesAlwaysVisible)
        storage.writeFlag(KEY_CONTRAST, settings.highContrast)
        storage.writeFlag(KEY_MOTION, settings.reducedMotion)
        storage.writeFlag(KEY_HINTS, settings.moveHints)
    }

    private companion object {
        const val KEY_REHEARSAL = "rehearsal_seconds"
        const val KEY_CONES = "cones_always_visible"
        const val KEY_CONTRAST = "high_contrast"
        const val KEY_MOTION = "reduced_motion"
        const val KEY_HINTS = "move_hints"
    }
}
