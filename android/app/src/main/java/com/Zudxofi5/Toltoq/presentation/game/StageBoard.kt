package com.Zudxofi5.Toltoq.presentation.game

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Zudxofi5.Toltoq.R
import com.Zudxofi5.Toltoq.core.config.GameConfig
import com.Zudxofi5.Toltoq.core.theme.AntiqueBrass
import com.Zudxofi5.Toltoq.core.theme.BoardWell
import com.Zudxofi5.Toltoq.core.theme.BrassDeep
import com.Zudxofi5.Toltoq.core.theme.ConeEdge
import com.Zudxofi5.Toltoq.core.theme.ConeFill
import com.Zudxofi5.Toltoq.core.theme.ConeFillStrong
import com.Zudxofi5.Toltoq.core.theme.GoldLight
import com.Zudxofi5.Toltoq.core.theme.ParquetDark
import com.Zudxofi5.Toltoq.core.theme.ParquetLight
import com.Zudxofi5.Toltoq.core.theme.RoyalGold
import com.Zudxofi5.Toltoq.core.theme.SpotlightFill
import com.Zudxofi5.Toltoq.core.theme.StageVoid
import com.Zudxofi5.Toltoq.core.theme.WallEdge
import com.Zudxofi5.Toltoq.core.theme.WallTone
import com.Zudxofi5.Toltoq.domain.model.Cell
import com.Zudxofi5.Toltoq.domain.model.CellKind
import com.Zudxofi5.Toltoq.domain.model.Facing
import kotlin.math.floor
import kotlin.math.sin

private val BoardPad = 6.dp
private val BoardBorder = 2.dp

@Composable
fun StageBoard(
    state: GameUiState,
    onTile: (Cell) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val frame = BoardPad + BoardBorder
        val widthBudget = minOf(maxWidth - 32.dp, 380.dp)
        val byWidth = floor((widthBudget - frame * 2).value / state.cols)
        val byHeight = floor((maxHeight - frame * 2).value / state.rows)
        val tile = minOf(byWidth, byHeight).coerceAtLeast(24f).dp
        val boardWidth = tile * state.cols + frame * 2
        val boardHeight = tile * state.rows + frame * 2
        BoardSurface(
            state = state,
            onTile = onTile,
            tile = tile,
            frame = frame,
            boardWidth = boardWidth,
            boardHeight = boardHeight
        )
    }
}

@Composable
private fun BoardSurface(
    state: GameUiState,
    onTile: (Cell) -> Unit,
    tile: Dp,
    frame: Dp,
    boardWidth: Dp,
    boardHeight: Dp
) {
    val transition = rememberInfiniteTransition(label = "boardLoop")
    val piecePulse by transition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            tween(2600, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "piecePulse"
    )
    val shakeTarget = if (state.flash == FlashKind.SPOTTED && !state.reducedMotion) 1f else 0f
    val shake by androidx.compose.animation.core.animateFloatAsState(
        targetValue = shakeTarget,
        animationSpec = tween(260),
        label = "shake"
    )
    val moveMs = if (state.reducedMotion) GameConfig.REDUCED_ANIM_MS else GameConfig.MOVE_ANIM_MS
    val jokerX by animateDpAsState(
        targetValue = tile * state.joker.col,
        animationSpec = tween(moveMs, easing = FastOutSlowInEasing),
        label = "jokerX"
    )
    val jokerY by animateDpAsState(
        targetValue = tile * state.joker.row,
        animationSpec = tween(moveMs, easing = FastOutSlowInEasing),
        label = "jokerY"
    )
    val shape = RoundedCornerShape(10.dp)

    Box(
        modifier = Modifier
            .offset(x = (sin(shake * 18f) * 4f).dp)
            .width(boardWidth)
            .height(boardHeight)
            .shadow(14.dp, shape, ambientColor = RoyalGold, spotColor = StageVoid)
            .background(BoardWell, shape)
            .border(
                width = BoardBorder,
                brush = Brush.verticalGradient(listOf(AntiqueBrass, RoyalGold, BrassDeep)),
                shape = shape
            )
            .padding(frame)
    ) {
        for (row in 0 until state.rows) {
            for (col in 0 until state.cols) {
                val cell = Cell(col, row)
                val kind = state.kinds.getOrElse(row * state.cols + col) { CellKind.FLOOR }
                val legal = state.moveHints && state.legalMoves.contains(cell)
                BoardTile(
                    cell = cell,
                    kind = kind,
                    tile = tile,
                    legal = legal,
                    highContrast = state.highContrast,
                    onTile = onTile,
                    interactive = state.legalMoves.contains(cell)
                )
            }
        }
        if (state.spotlightCol >= 0) {
            for (offsetCol in 0..1) {
                val col = state.spotlightCol + offsetCol
                if (col < state.cols) {
                    Box(
                        modifier = Modifier
                            .offset(x = tile * col)
                            .width(tile)
                            .height(tile * state.rows)
                            .background(
                                if (state.highContrast) {
                                    SpotlightFill.copy(alpha = 0.34f)
                                } else {
                                    SpotlightFill
                                }
                            )
                    )
                }
            }
        }
        if (state.conesVisible) {
            for (cone in state.coneCells) {
                Box(
                    modifier = Modifier
                        .offset(x = tile * cone.col, y = tile * cone.row)
                        .size(tile)
                        .background(if (state.highContrast) ConeFillStrong else ConeFill)
                        .border(1.dp, ConeEdge)
                )
            }
        }
        for (piece in state.pieces) {
            Box(
                modifier = Modifier
                    .offset(x = tile * piece.col, y = tile * piece.row)
                    .size(tile),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_4),
                    contentDescription = stringResource(R.string.cd_costume_piece),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(tile - 20.dp)
                        .scale(if (state.reducedMotion) 1f else piecePulse)
                )
            }
        }
        Box(
            modifier = Modifier
                .offset(x = tile * state.door.col, y = tile * state.door.row)
                .size(tile),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_5),
                contentDescription = stringResource(R.string.cd_stage_door),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(tile - 6.dp)
            )
        }
        val crowd = if (state.phase == GamePhase.REHEARSAL && state.ghosts.isNotEmpty()) {
            state.ghosts
        } else {
            state.ushers
        }
        crowd.forEachIndexed { index, usher ->
            key(index) {
                UsherToken(
                    col = usher.cell.col,
                    row = usher.cell.row,
                    facing = usher.facing,
                    tile = tile,
                    ghost = state.phase == GamePhase.REHEARSAL,
                    lured = usher.lureTurns > 0,
                    reducedMotion = state.reducedMotion
                )
            }
        }
        Box(
            modifier = Modifier
                .offset(x = jokerX, y = jokerY)
                .size(tile),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_1),
                contentDescription = stringResource(R.string.cd_performer),
                contentScale = ContentScale.Fit,
                colorFilter = if (state.goldTrim) ColorFilter.tint(GoldLight, BlendModeTint) else null,
                modifier = Modifier
                    .size(tile - 8.dp)
                    .alpha(if (state.hidden) 0.72f else 1f)
            )
        }
        if (state.flash != FlashKind.NONE) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        if (state.flash == FlashKind.SPOTTED) {
                            ConeFillStrong
                        } else {
                            GoldLight.copy(alpha = 0.33f)
                        }
                    )
            )
        }
    }
}

private val BlendModeTint = androidx.compose.ui.graphics.BlendMode.Overlay

@Composable
private fun BoardTile(
    cell: Cell,
    kind: CellKind,
    tile: Dp,
    legal: Boolean,
    highContrast: Boolean,
    interactive: Boolean,
    onTile: (Cell) -> Unit
) {
    val base = when (kind) {
        CellKind.WALL -> WallTone
        CellKind.PROP -> ParquetDark
        CellKind.FLOOR -> if ((cell.col + cell.row) % 2 == 0) ParquetDark else ParquetLight
    }
    Box(
        modifier = Modifier
            .offset(x = tile * cell.col, y = tile * cell.row)
            .size(tile)
            .padding(1.dp)
            .background(
                if (legal) RoyalGold.copy(alpha = 0.22f) else base,
                RoundedCornerShape(2.dp)
            )
            .border(
                width = if (highContrast) 1.5.dp else 1.dp,
                color = when {
                    legal -> RoyalGold
                    kind == CellKind.WALL -> WallEdge
                    else -> Color.Transparent
                },
                shape = RoundedCornerShape(2.dp)
            )
            .clickable(enabled = interactive) { onTile(cell) },
        contentAlignment = Alignment.Center
    ) {
        if (kind == CellKind.PROP) {
            Image(
                painter = painterResource(R.drawable.sprite_3),
                contentDescription = stringResource(R.string.cd_prop_trunk),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(tile - 10.dp)
            )
        }
    }
}

@Composable
private fun UsherToken(
    col: Int,
    row: Int,
    facing: Facing,
    tile: Dp,
    ghost: Boolean,
    lured: Boolean,
    reducedMotion: Boolean
) {
    val stepMs = if (reducedMotion) GameConfig.REDUCED_ANIM_MS else GameConfig.USHER_ANIM_MS
    val x by animateDpAsState(
        targetValue = tile * col,
        animationSpec = tween(stepMs),
        label = "usherX"
    )
    val y by animateDpAsState(
        targetValue = tile * row,
        animationSpec = tween(stepMs),
        label = "usherY"
    )
    Box(
        modifier = Modifier
            .offset(x = x, y = y)
            .size(tile),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.sprite_2),
            contentDescription = stringResource(R.string.cd_usher),
            contentScale = ContentScale.Fit,
            colorFilter = if (lured) ColorFilter.tint(RoyalGold, BlendModeTint) else null,
            modifier = Modifier
                .size(tile - 8.dp)
                .alpha(if (ghost) 0.45f else 1f)
                .graphicsLayer {
                    scaleX = if (facing == Facing.LEFT) -1f else 1f
                }
        )
    }
}
