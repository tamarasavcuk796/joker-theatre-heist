package com.Zudxofi5.Toltoq.domain.model

data class Cell(val col: Int, val row: Int)

enum class CellKind { FLOOR, WALL, PROP }

enum class Facing { UP, DOWN, LEFT, RIGHT }

fun Facing.deltaCol(): Int = when (this) {
    Facing.LEFT -> -1
    Facing.RIGHT -> 1
    else -> 0
}

fun Facing.deltaRow(): Int = when (this) {
    Facing.UP -> -1
    Facing.DOWN -> 1
    else -> 0
}

fun manhattan(a: Cell, b: Cell): Int = kotlin.math.abs(a.col - b.col) + kotlin.math.abs(a.row - b.row)
