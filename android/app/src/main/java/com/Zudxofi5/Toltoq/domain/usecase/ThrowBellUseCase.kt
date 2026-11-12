package com.Zudxofi5.Toltoq.domain.usecase

import com.Zudxofi5.Toltoq.domain.model.RunState
import com.Zudxofi5.Toltoq.domain.model.manhattan

class ThrowBellUseCase {
    operator fun invoke(run: RunState, range: Int, lureTurns: Int): RunState {
        if (run.bellsLeft <= 0) return run
        val target = run.joker
        val nearest = run.ushers
            .withIndex()
            .filter { manhattan(it.value.cell, target) <= range }
            .minByOrNull { manhattan(it.value.cell, target) }
            ?: return run.copy(bellsLeft = run.bellsLeft - 1)
        val lured = run.ushers.toMutableList()
        lured[nearest.index] = nearest.value.copy(lureCell = target, lureTurns = lureTurns)
        return run.copy(ushers = lured, bellsLeft = run.bellsLeft - 1)
    }
}
