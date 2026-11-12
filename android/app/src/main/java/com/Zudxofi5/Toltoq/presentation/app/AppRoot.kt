package com.Zudxofi5.Toltoq.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Zudxofi5.Toltoq.core.di.AppViewModelFactory
import com.Zudxofi5.Toltoq.core.navigation.Screen
import com.Zudxofi5.Toltoq.core.theme.StageVoid
import com.Zudxofi5.Toltoq.presentation.collection.CollectionRoute
import com.Zudxofi5.Toltoq.presentation.game.GameRoute
import com.Zudxofi5.Toltoq.presentation.levelselect.LevelSelectRoute
import com.Zudxofi5.Toltoq.presentation.menu.MenuRoute
import com.Zudxofi5.Toltoq.presentation.progress.ProgressRoute
import com.Zudxofi5.Toltoq.presentation.result.ResultRoute
import com.Zudxofi5.Toltoq.presentation.settings.SettingsRoute
import com.Zudxofi5.Toltoq.presentation.splash.SplashRoute

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = AppViewModelFactory())
    val screen by appViewModel.screen.collectAsStateWithLifecycle()

    BackHandler(enabled = screen !is Screen.Splash && screen !is Screen.Menu) {
        if (!appViewModel.back()) appViewModel.toMenu()
    }

    AnimatedContent(
        targetState = screen,
        transitionSpec = { fadeIn(tween(420)) togetherWith fadeOut(tween(280)) },
        label = "appRoot",
        modifier = Modifier
            .fillMaxSize()
            .background(StageVoid)
    ) { target ->
        when (target) {
            is Screen.Splash -> SplashRoute(
                onFinished = { appViewModel.openMenuFromSplash() }
            )
            is Screen.Menu -> MenuRoute(
                onPlay = { levelId -> appViewModel.startLevel(levelId) },
                onLevels = { appViewModel.navigate(Screen.LevelSelect) },
                onCollection = { appViewModel.navigate(Screen.Collection) },
                onProgress = { appViewModel.navigate(Screen.Progress) },
                onSettings = { appViewModel.navigate(Screen.Settings) }
            )
            is Screen.LevelSelect -> LevelSelectRoute(
                onPlayLevel = { levelId -> appViewModel.startLevel(levelId) },
                onMenu = { appViewModel.toMenu() }
            )
            is Screen.Game -> GameRoute(
                levelId = target.levelId,
                attempt = target.attempt,
                onFinished = { outcome -> appViewModel.finishRun(Screen.Result(outcome)) },
                onMenu = { appViewModel.toMenu() }
            )
            is Screen.Result -> ResultRoute(
                run = target.run,
                onPlayAgain = {
                    appViewModel.replayLevel(target.run.levelId)
                },
                onNextLevel = { levelId -> appViewModel.replayLevel(levelId) },
                onLevels = { appViewModel.openLevelSelect() },
                onMenu = { appViewModel.toMenu() }
            )
            is Screen.Collection -> CollectionRoute(onMenu = { appViewModel.toMenu() })
            is Screen.Progress -> ProgressRoute(onMenu = { appViewModel.toMenu() })
            is Screen.Settings -> SettingsRoute(onMenu = { appViewModel.toMenu() })
        }
    }
}
