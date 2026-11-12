package com.Zudxofi5.Toltoq.presentation.levelselect

import com.Zudxofi5.Toltoq.domain.model.ActProgress

data class LevelPlaque(
    val levelId: Int,
    val act: Int,
    val nameRes: Int,
    val stars: Int,
    val unlocked: Boolean
)

data class LevelSelectUiState(
    val acts: List<ActProgress> = emptyList(),
    val plaques: List<LevelPlaque> = emptyList(),
    val selectedAct: Int = 1
)
