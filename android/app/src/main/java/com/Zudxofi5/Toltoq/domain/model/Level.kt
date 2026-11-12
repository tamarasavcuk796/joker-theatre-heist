package com.Zudxofi5.Toltoq.domain.model

data class Level(
    val id: Int,
    val act: Int,
    val nameRes: Int,
    val par: Int,
    val budget: Int,
    val bells: Int,
    val spotlight: Boolean,
    val cols: Int,
    val rows: Int,
    val kinds: List<CellKind>,
    val start: Cell,
    val door: Cell,
    val pieces: Set<Cell>,
    val ushers: List<Usher>
) {
    fun inBounds(cell: Cell): Boolean = cell.col in 0 until cols && cell.row in 0 until rows

    fun kindAt(cell: Cell): CellKind =
        if (inBounds(cell)) kinds[cell.row * cols + cell.col] else CellKind.WALL
}

fun visionCells(usher: Usher, level: Level, range: Int): List<Cell> {
    val out = ArrayList<Cell>()
    var cursor = usher.cell
    for (step in 1..range) {
        cursor = Cell(cursor.col + usher.facing.deltaCol(), cursor.row + usher.facing.deltaRow())
        if (!level.inBounds(cursor)) break
        val kind = level.kindAt(cursor)
        if (kind != CellKind.FLOOR) break
        out.add(cursor)
    }
    return out
}
