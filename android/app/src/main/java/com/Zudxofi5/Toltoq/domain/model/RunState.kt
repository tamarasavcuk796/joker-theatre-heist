package com.Zudxofi5.Toltoq.domain.model

enum class RunOutcome { ESCAPED, SPOTTED, OUT_OF_MOVES }

data class RunState(
    val joker: Cell,
    val lastCell: Cell,
    val ushers: List<Usher>,
    val pieces: Set<Cell>,
    val piecesHeld: Int,
    val movesLeft: Int,
    val bellsLeft: Int,
    val spotlightCol: Int,
    val turn: Int,
    val masqueradeReady: Boolean
) {
    companion object {
        fun opening(level: Level): RunState = RunState(
            joker = level.start,
            lastCell = level.start,
            ushers = level.ushers,
            pieces = level.pieces,
            piecesHeld = 0,
            movesLeft = level.budget,
            bellsLeft = level.bells,
            spotlightCol = -1,
            turn = 0,
            masqueradeReady = false
        )
    }
}

data class RunResult(
    val levelId: Int,
    val outcome: RunOutcome,
    val score: Int,
    val stars: Int,
    val pieces: Int,
    val movesLeft: Int,
    val movesUsed: Int,
    val usedBell: Boolean,
    val newBest: Boolean
)
