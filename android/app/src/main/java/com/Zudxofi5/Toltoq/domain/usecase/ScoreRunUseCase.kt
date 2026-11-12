package com.Zudxofi5.Toltoq.domain.usecase

import com.Zudxofi5.Toltoq.domain.model.Level
import com.Zudxofi5.Toltoq.domain.model.RunOutcome
import com.Zudxofi5.Toltoq.domain.model.RunResult
import com.Zudxofi5.Toltoq.domain.model.RunState

class ScoreRunUseCase {
    operator fun invoke(
        level: Level,
        run: RunState,
        outcome: RunOutcome,
        usedBell: Boolean,
        piecesTarget: Int
    ): RunResult {
        val movesUsed = level.budget - run.movesLeft
        if (outcome != RunOutcome.ESCAPED) {
            return RunResult(
                levelId = level.id,
                outcome = outcome,
                score = 0,
                stars = 0,
                pieces = run.piecesHeld,
                movesLeft = run.movesLeft,
                movesUsed = movesUsed,
                usedBell = usedBell,
                newBest = false
            )
        }
        val stars = when {
            movesUsed <= level.par && run.piecesHeld >= piecesTarget -> 3
            movesUsed <= level.par + 4 -> 2
            else -> 1
        }
        val cleanBonus = if (usedBell) 0 else 250
        val score = 1000 + run.piecesHeld * 500 + run.movesLeft * 25 + cleanBonus
        return RunResult(
            levelId = level.id,
            outcome = outcome,
            score = score,
            stars = stars,
            pieces = run.piecesHeld,
            movesLeft = run.movesLeft,
            movesUsed = movesUsed,
            usedBell = usedBell,
            newBest = false
        )
    }
}
