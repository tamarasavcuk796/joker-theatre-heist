package com.Zudxofi5.Toltoq.core.di

import android.content.Context
import com.Zudxofi5.Toltoq.data.local.PreferencesStorage
import com.Zudxofi5.Toltoq.data.repository.LevelRepositoryImpl
import com.Zudxofi5.Toltoq.data.repository.ProgressRepositoryImpl
import com.Zudxofi5.Toltoq.data.repository.SettingsRepositoryImpl
import com.Zudxofi5.Toltoq.domain.repository.LevelRepository
import com.Zudxofi5.Toltoq.domain.repository.ProgressRepository
import com.Zudxofi5.Toltoq.domain.repository.SettingsRepository

object ServiceLocator {

    private var storage: PreferencesStorage? = null
    private var progress: ProgressRepository? = null
    private var settings: SettingsRepository? = null
    private var levels: LevelRepository? = null

    fun init(context: Context) {
        if (storage == null) {
            val created = PreferencesStorage(context.applicationContext)
            storage = created
            progress = ProgressRepositoryImpl(created)
            settings = SettingsRepositoryImpl(created)
            levels = LevelRepositoryImpl()
        }
    }

    fun progressRepository(): ProgressRepository =
        progress ?: throw IllegalStateException(NOT_READY)

    fun settingsRepository(): SettingsRepository =
        settings ?: throw IllegalStateException(NOT_READY)

    fun levelRepository(): LevelRepository =
        levels ?: throw IllegalStateException(NOT_READY)

    private const val NOT_READY = "ServiceLocator.init was not called"
}
