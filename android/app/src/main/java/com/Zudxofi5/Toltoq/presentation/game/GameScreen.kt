package com.Zudxofi5.Toltoq.presentation.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Zudxofi5.Toltoq.R
import com.Zudxofi5.Toltoq.core.di.AppViewModelFactory
import com.Zudxofi5.Toltoq.core.theme.GoldLight
import com.Zudxofi5.Toltoq.core.theme.RoyalGold
import com.Zudxofi5.Toltoq.core.theme.StageInk
import com.Zudxofi5.Toltoq.core.theme.StageVoid
import com.Zudxofi5.Toltoq.core.theme.SurfaceBrass
import com.Zudxofi5.Toltoq.core.theme.SurfaceVelvet
import com.Zudxofi5.Toltoq.core.theme.TextPrimary
import com.Zudxofi5.Toltoq.core.theme.TextSecondary
import com.Zudxofi5.Toltoq.domain.model.Cell
import com.Zudxofi5.Toltoq.domain.model.RunResult
import com.Zudxofi5.Toltoq.presentation.dialog.ConfirmDialog
import com.Zudxofi5.Toltoq.presentation.dialog.PauseDialog

@Composable
fun GameRoute(
    levelId: Int,
    attempt: Int,
    onFinished: (RunResult) -> Unit,
    onMenu: () -> Unit
) {
    val gameViewModel: GameViewModel = viewModel(
        key = "game-" + levelId + "-" + attempt,
        factory = AppViewModelFactory(levelId)
    )
    val state by gameViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        gameViewModel.events.collect { outcome ->
            onFinished(outcome)
        }
    }
    DisposableEffect(Unit) {
        onDispose { gameViewModel.stop() }
    }
    GameScreen(
        state = state,
        onTile = gameViewModel::onTile,
        onBell = gameViewModel::onBell,
        onWait = gameViewModel::onWait,
        onAskRestart = gameViewModel::onAskRestart,
        onDismissRestart = gameViewModel::onDismissRestart,
        onRestart = gameViewModel::onRestart,
        onPause = gameViewModel::onPause,
        onResume = gameViewModel::onResume,
        onMenu = onMenu
    )
}

@Composable
private fun GameScreen(
    state: GameUiState,
    onTile: (Cell) -> Unit,
    onBell: () -> Unit,
    onWait: () -> Unit,
    onAskRestart: () -> Unit,
    onDismissRestart: () -> Unit,
    onRestart: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onMenu: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(StageVoid)
    ) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.08f)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(StageInk, StageVoid, SurfaceVelvet)
                    )
                )
                .alpha(0.86f)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            GameHeader(state = state, onPause = onPause)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                StageBoard(
                    state = state,
                    onTile = onTile,
                    modifier = Modifier.fillMaxSize()
                )
                if (state.phase == GamePhase.REHEARSAL) {
                    RehearsalOverlay(secondsLeft = state.rehearsalSecondsLeft)
                }
                if (state.phase == GamePhase.PLAYING && state.showControlHint) {
                    ControlHint(modifier = Modifier.align(Alignment.BottomCenter))
                }
            }
            ControlBar(
                bellsLeft = state.bellsLeft,
                enabled = state.phase == GamePhase.PLAYING,
                onBell = onBell,
                onWait = onWait,
                onRestart = onAskRestart,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
            )
        }
        if (state.paused) {
            PauseDialog(
                movesLeft = state.movesLeft,
                piecesHeld = state.piecesHeld,
                piecesTarget = state.piecesTarget,
                onResume = onResume,
                onRestart = onRestart,
                onMenu = onMenu
            )
        }
        if (state.confirmRestart) {
            ConfirmDialog(
                title = stringResource(R.string.restart_title),
                message = stringResource(R.string.restart_message),
                confirmLabel = stringResource(R.string.cta_restart),
                onConfirm = onRestart,
                onDismiss = onDismissRestart
            )
        }
    }
}

@Composable
private fun GameHeader(state: GameUiState, onPause: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(StageInk.copy(alpha = 0.65f))
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(SurfaceBrass, CircleShape)
                .clickable(onClick = onPause),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.icon_pause),
                contentDescription = stringResource(R.string.cd_pause),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(24.dp)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.header_act, state.act, state.levelId),
                style = MaterialTheme.typography.labelLarge,
                color = GoldLight,
                textAlign = TextAlign.Center
            )
            if (state.roomNameRes != 0) {
                Text(
                    text = stringResource(state.roomNameRes),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = stringResource(R.string.header_moves, state.movesLeft),
                style = MaterialTheme.typography.labelLarge,
                color = TextPrimary,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_4),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = stringResource(
                        R.string.fraction,
                        state.piecesHeld,
                        state.piecesTarget
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (state.masqueradeReady) GoldLight else TextSecondary,
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                )
            }
        }
    }
}

@Composable
private fun RehearsalOverlay(secondsLeft: Int) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(StageVoid.copy(alpha = 0.85f)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.rehearsal_title),
                style = MaterialTheme.typography.titleLarge,
                color = GoldLight,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.rehearsal_countdown, secondsLeft),
                style = MaterialTheme.typography.displaySmall,
                color = RoyalGold
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.rehearsal_hint),
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ControlHint(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.control_hint),
        style = MaterialTheme.typography.labelSmall,
        color = GoldLight,
        textAlign = TextAlign.Center,
        modifier = modifier
            .padding(bottom = 4.dp)
            .background(StageInk.copy(alpha = 0.72f), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    )
}
