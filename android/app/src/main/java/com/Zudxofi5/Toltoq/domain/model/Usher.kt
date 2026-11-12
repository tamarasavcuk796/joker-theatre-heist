package com.Zudxofi5.Toltoq.domain.model

data class Usher(
    val route: List<Cell>,
    val index: Int,
    val cell: Cell,
    val facing: Facing,
    val lureCell: Cell? = null,
    val lureTurns: Int = 0
) {
    fun step(): Usher {
        val lure = lureCell
        if (lureTurns > 0 && lure != null) {
            return copy(facing = facingTowards(cell, lure), lureTurns = lureTurns - 1)
        }
        if (route.size < 2) return copy(lureTurns = 0, lureCell = null)
        val nextIndex = (index + 1) % route.size
        val target = route[nextIndex]
        return copy(
            index = nextIndex,
            cell = target,
            facing = facingTowards(cell, target),
            lureTurns = 0,
            lureCell = null
        )
    }

    private fun facingTowards(from: Cell, to: Cell): Facing = when {
        to.col > from.col -> Facing.RIGHT
        to.col < from.col -> Facing.LEFT
        to.row > from.row -> Facing.DOWN
        to.row < from.row -> Facing.UP
        else -> facing
    }

    companion object {
        fun onRoute(route: List<Cell>, startIndex: Int): Usher {
            val safeIndex = if (route.isEmpty()) 0 else startIndex % route.size
            val cell = if (route.isEmpty()) Cell(0, 0) else route[safeIndex]
            val next = if (route.size < 2) cell else route[(safeIndex + 1) % route.size]
            val facing = when {
                next.col > cell.col -> Facing.RIGHT
                next.col < cell.col -> Facing.LEFT
                next.row > cell.row -> Facing.DOWN
                else -> Facing.UP
            }
            return Usher(route, safeIndex, cell, facing)
        }
    }
}
