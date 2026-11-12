package com.Zudxofi5.Toltoq.core.navigation

import com.Zudxofi5.Toltoq.domain.model.RunResult

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data object LevelSelect : Screen
    data class Game(val levelId: Int, val attempt: Int) : Screen
    data class Result(val run: RunResult) : Screen
    data object Collection : Screen
    data object Progress : Screen
    data object Settings : Screen
}
