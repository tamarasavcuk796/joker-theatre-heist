package com.Zudxofi5.Toltoq.presentation.collection

import com.Zudxofi5.Toltoq.domain.model.CostumeProgress

data class CollectionUiState(
    val costumes: List<CostumeProgress> = emptyList(),
    val pieces: Int = 0,
    val maxPieces: Int = 0
)
