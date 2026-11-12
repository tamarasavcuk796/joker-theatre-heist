package com.Zudxofi5.Toltoq.presentation.app

import androidx.lifecycle.ViewModel
import com.Zudxofi5.Toltoq.core.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val stack = ArrayDeque<Screen>()
    private var attempts = 0

    fun navigate(target: Screen) {
        val current = _screen.value
        if (current !is Screen.Splash) stack.addLast(current)
        _screen.value = target
    }

    fun replace(target: Screen) {
        _screen.value = target
    }

    fun back(): Boolean {
        val previous = stack.removeLastOrNull() ?: return false
        _screen.value = previous
        return true
    }

    fun toMenu() {
        stack.clear()
        _screen.value = Screen.Menu
    }

    fun openMenuFromSplash() {
        stack.clear()
        _screen.value = Screen.Menu
    }

    fun openLevelSelect() {
        stack.clear()
        stack.addLast(Screen.Menu)
        _screen.value = Screen.LevelSelect
    }

    fun startLevel(levelId: Int) {
        attempts += 1
        navigate(Screen.Game(levelId, attempts))
    }

    fun replayLevel(levelId: Int) {
        attempts += 1
        _screen.value = Screen.Game(levelId, attempts)
    }

    fun finishRun(target: Screen) {
        _screen.value = target
    }
}
