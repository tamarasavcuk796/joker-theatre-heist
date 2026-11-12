package com.Zudxofi5.Toltoq.domain.model

data class LevelProgress(
    val levelId: Int,
    val stars: Int,
    val bestScore: Int,
    val unlocked: Boolean
)

data class ActProgress(
    val act: Int,
    val stars: Int,
    val maxStars: Int,
    val unlocked: Boolean
)

data class CareerProgress(
    val totalScore: Int,
    val stars: Int,
    val maxStars: Int,
    val levelsCleared: Int,
    val totalLevels: Int,
    val bestEscapeMoves: Int,
    val cleanRuns: Int,
    val pieces: Int,
    val maxPieces: Int
)

data class Costume(
    val id: Int,
    val nameRes: Int
)

data class CostumeProgress(
    val costume: Costume,
    val pieces: Int,
    val required: Int,
    val equipped: Boolean
) {
    val complete: Boolean get() = pieces >= required
}

data class GameSettings(
    val rehearsalSeconds: Int = 5,
    val conesAlwaysVisible: Boolean = false,
    val highContrast: Boolean = false,
    val reducedMotion: Boolean = false,
    val moveHints: Boolean = true
)
