package com.Zudxofi5.Toltoq.domain.usecase

import com.Zudxofi5.Toltoq.domain.model.Level
import com.Zudxofi5.Toltoq.domain.model.RunState

class AdvanceTurnUseCase {
    operator fun invoke(level: Level, run: RunState): RunState {
        val turn = run.turn + 1
        val span = if (level.cols > 1) level.cols - 1 else 1
        val spotlight = if (level.spotlight) (turn / 2) % span else -1
        return run.copy(
            ushers = run.ushers.map { it.step() },
            turn = turn,
            spotlightCol = spotlight
        )
    }
}
