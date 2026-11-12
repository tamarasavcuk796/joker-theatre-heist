package com.Zudxofi5.Toltoq.presentation.menu

data class MenuUiState(
    val levelsCleared: Int = 0,
    val totalLevels: Int = 0,
    val bestScore: Int = 0,
    val pieces: Int = 0,
    val maxPieces: Int = 0,
    val nextLevelId: Int = 1
)
