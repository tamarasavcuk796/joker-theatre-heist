package com.Zudxofi5.Toltoq.domain.usecase

import com.Zudxofi5.Toltoq.domain.model.CellKind
import com.Zudxofi5.Toltoq.domain.model.Level
import com.Zudxofi5.Toltoq.domain.model.RunState
import com.Zudxofi5.Toltoq.domain.model.visionCells

class ResolveDetectionUseCase {
    operator fun invoke(level: Level, run: RunState, range: Int): Boolean {
        if (run.ushers.any { it.cell == run.joker }) return true
        if (level.kindAt(run.joker) == CellKind.PROP) return false
        val lit = run.spotlightCol >= 0 &&
            (run.joker.col == run.spotlightCol || run.joker.col == run.spotlightCol + 1)
        if (lit) return true
        return run.ushers.any { usher -> visionCells(usher, level, range).contains(run.joker) }
    }
}
