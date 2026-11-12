package com.Zudxofi5.Toltoq.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.Zudxofi5.Toltoq.presentation.app.AppViewModel
import com.Zudxofi5.Toltoq.presentation.collection.CollectionViewModel
import com.Zudxofi5.Toltoq.presentation.game.GameViewModel
import com.Zudxofi5.Toltoq.presentation.levelselect.LevelSelectViewModel
import com.Zudxofi5.Toltoq.presentation.menu.MenuViewModel
import com.Zudxofi5.Toltoq.presentation.progress.ProgressViewModel
import com.Zudxofi5.Toltoq.presentation.result.ResultViewModel
import com.Zudxofi5.Toltoq.presentation.settings.SettingsViewModel
import com.Zudxofi5.Toltoq.presentation.splash.SplashViewModel

class AppViewModelFactory(private val levelId: Int = 1) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) -> AppViewModel()
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel()
            modelClass.isAssignableFrom(MenuViewModel::class.java) ->
                MenuViewModel(ServiceLocator.progressRepository())
            modelClass.isAssignableFrom(LevelSelectViewModel::class.java) ->
                LevelSelectViewModel(ServiceLocator.progressRepository(), ServiceLocator.levelRepository())
            modelClass.isAssignableFrom(GameViewModel::class.java) ->
                GameViewModel(
                    levelId,
                    ServiceLocator.levelRepository(),
                    ServiceLocator.progressRepository(),
                    ServiceLocator.settingsRepository()
                )
            modelClass.isAssignableFrom(ResultViewModel::class.java) ->
                ResultViewModel(ServiceLocator.progressRepository(), ServiceLocator.levelRepository())
            modelClass.isAssignableFrom(CollectionViewModel::class.java) ->
                CollectionViewModel(ServiceLocator.progressRepository())
            modelClass.isAssignableFrom(ProgressViewModel::class.java) ->
                ProgressViewModel(ServiceLocator.progressRepository())
            modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
                SettingsViewModel(ServiceLocator.settingsRepository(), ServiceLocator.progressRepository())
            else -> throw IllegalArgumentException(modelClass.name)
        }
        return created as T
    }
}
