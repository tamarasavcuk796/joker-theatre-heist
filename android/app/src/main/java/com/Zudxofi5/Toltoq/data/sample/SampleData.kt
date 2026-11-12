package com.Zudxofi5.Toltoq.data.sample

import com.Zudxofi5.Toltoq.R
import com.Zudxofi5.Toltoq.core.config.GameConfig
import com.Zudxofi5.Toltoq.domain.model.Cell
import com.Zudxofi5.Toltoq.domain.model.CellKind
import com.Zudxofi5.Toltoq.domain.model.Costume
import com.Zudxofi5.Toltoq.domain.model.Level
import com.Zudxofi5.Toltoq.domain.model.Usher

object SampleData {

    val costumes: List<Costume> = listOf(
        Costume(0, R.string.costume_harlequin),
        Costume(1, R.string.costume_domino),
        Costume(2, R.string.costume_pierrot),
        Costume(3, R.string.costume_bells),
        Costume(4, R.string.costume_columbine),
        Costume(5, R.string.costume_capitano),
        Costume(6, R.string.costume_scaramouche),
        Costume(7, R.string.costume_pantalone)
    )

    val levels: List<Level> = listOf(
        build(
            id = 1, act = 1, nameRes = R.string.room_prompt_box, par = 16, bells = 2, spotlight = false,
            rows = listOf("....D.", "..#...", "......", ".#..#.", "..P...", "...T..", ".P#.P.", "J....."),
            routes = listOf(horizontal(2, 1, 4))
        ),
        build(
            id = 2, act = 1, nameRes = R.string.room_foyer, par = 16, bells = 2, spotlight = false,
            rows = listOf("D.....", "..#...", ".P....", "..#...", "....T.", ".P..P.", "..##..", ".....J"),
            routes = listOf(vertical(3, 1, 5))
        ),
        build(
            id = 3, act = 1, nameRes = R.string.room_costume_vault, par = 18, bells = 2, spotlight = true,
            rows = listOf("..D...", ".#.#..", "..P...", "#...#.", "...T..", "......", ".#.P#.", "J...P."),
            routes = listOf(horizontal(5, 0, 4))
        ),
        build(
            id = 4, act = 1, nameRes = R.string.room_orchestra_pit, par = 18, bells = 2, spotlight = true,
            rows = listOf("....D.", "..#.#.", ".P....", "......", ".#.#..", "..P...", ".#..T.", "J..P.."),
            routes = listOf(horizontal(3, 1, 5))
        ),
        build(
            id = 5, act = 2, nameRes = R.string.room_mirror_corridor, par = 22, bells = 1, spotlight = true,
            rows = listOf("...D..", "..#..#", "......", "#..#..", ".....P", "..#...", "..#T..", "J.P..P"),
            routes = listOf(horizontal(2, 1, 4), vertical(1, 4, 7))
        ),
        build(
            id = 6, act = 2, nameRes = R.string.room_chandelier_loft, par = 22, bells = 1, spotlight = true,
            rows = listOf(".D....", "..#...", ".P....", "#.#...", "......", "..#...", ".P#.P.", "J....."),
            routes = listOf(horizontal(4, 0, 4), vertical(4, 1, 5))
        ),
        build(
            id = 7, act = 2, nameRes = R.string.room_royal_box, par = 24, bells = 1, spotlight = true,
            rows = listOf("....D.", "......", "..#.#.", ".P....", "#..#..", "......", "..T.#.", "J..P.P"),
            routes = listOf(horizontal(1, 1, 5), horizontal(5, 0, 4))
        ),
        build(
            id = 8, act = 2, nameRes = R.string.room_scenery_dock, par = 26, bells = 1, spotlight = true,
            rows = listOf("D.....", ".#....", "....#.", ".#.#..", "..P...", "....#.", "......", ".P..PJ"),
            routes = listOf(vertical(2, 0, 5), horizontal(6, 1, 5))
        ),
        build(
            id = 9, act = 3, nameRes = R.string.room_fly_tower, par = 28, bells = 1, spotlight = true,
            rows = listOf("..D...", "......", "..#...", ".#....", "...#..", ".P....", "......", "J..P.P"),
            routes = listOf(horizontal(1, 0, 5), vertical(5, 2, 6), horizontal(6, 0, 4))
        ),
        build(
            id = 10, act = 3, nameRes = R.string.room_velvet_staircase, par = 30, bells = 1, spotlight = true,
            rows = listOf("....D.", "..#.#.", "......", "#..#..", "......", ".....P", "..#T..", "J.P..P"),
            routes = listOf(horizontal(2, 1, 5), horizontal(4, 0, 4), vertical(1, 5, 7))
        ),
        build(
            id = 11, act = 3, nameRes = R.string.room_paint_frame, par = 32, bells = 1, spotlight = true,
            rows = listOf("..D...", "......", "..#...", "......", "..#T..", ".P....", "......", "J.P..P"),
            routes = listOf(vertical(0, 1, 6), horizontal(3, 1, 5), vertical(5, 1, 6))
        ),
        build(
            id = 12, act = 3, nameRes = R.string.room_under_the_stage, par = 34, bells = 1, spotlight = true,
            rows = listOf("D.....", "......", "..#...", ".#..P.", "..P.#.", ".#....", "......", ".P.T.J"),
            routes = listOf(horizontal(1, 0, 5), vertical(3, 2, 6), horizontal(6, 0, 5))
        )
    )

    private fun horizontal(row: Int, from: Int, to: Int): List<Cell> {
        val path = ArrayList<Cell>()
        for (col in from..to) path.add(Cell(col, row))
        for (col in to - 1 downTo from + 1) path.add(Cell(col, row))
        return path
    }

    private fun vertical(col: Int, from: Int, to: Int): List<Cell> {
        val path = ArrayList<Cell>()
        for (row in from..to) path.add(Cell(col, row))
        for (row in to - 1 downTo from + 1) path.add(Cell(col, row))
        return path
    }

    private fun build(
        id: Int,
        act: Int,
        nameRes: Int,
        par: Int,
        bells: Int,
        spotlight: Boolean,
        rows: List<String>,
        routes: List<List<Cell>>
    ): Level {
        val cols = GameConfig.BOARD_COLS
        val kinds = ArrayList<CellKind>(cols * rows.size)
        val pieces = LinkedHashSet<Cell>()
        var start = Cell(0, rows.size - 1)
        var door = Cell(cols - 1, 0)
        for (row in rows.indices) {
            val line = rows[row]
            for (col in 0 until cols) {
                val symbol = if (col < line.length) line[col] else '.'
                val cell = Cell(col, row)
                when (symbol) {
                    '#' -> kinds.add(CellKind.WALL)
                    'T' -> kinds.add(CellKind.PROP)
                    else -> kinds.add(CellKind.FLOOR)
                }
                when (symbol) {
                    'P' -> pieces.add(cell)
                    'J' -> start = cell
                    'D' -> door = cell
                    else -> Unit
                }
            }
        }
        val ushers = routes.mapIndexed { index, route ->
            Usher.onRoute(route, (id + index * 2) % route.size)
        }
        return Level(
            id = id,
            act = act,
            nameRes = nameRes,
            par = par,
            budget = par + GameConfig.MOVE_BUDGET_SLACK,
            bells = bells,
            spotlight = spotlight,
            cols = cols,
            rows = rows.size,
            kinds = kinds,
            start = start,
            door = door,
            pieces = pieces,
            ushers = ushers
        )
    }
}
