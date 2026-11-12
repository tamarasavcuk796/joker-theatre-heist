package com.Zudxofi5.Toltoq.presentation.progress

import com.Zudxofi5.Toltoq.domain.model.ActProgress
import com.Zudxofi5.Toltoq.domain.model.CareerProgress

data class ProgressUiState(
    val career: CareerProgress? = null,
    val acts: List<ActProgress> = emptyList()
)
