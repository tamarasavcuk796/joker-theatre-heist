package com.Zudxofi5.Toltoq.presentation.result

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Zudxofi5.Toltoq.R
import com.Zudxofi5.Toltoq.core.di.AppViewModelFactory
import com.Zudxofi5.Toltoq.core.theme.DangerRed
import com.Zudxofi5.Toltoq.core.theme.GoldLight
import com.Zudxofi5.Toltoq.core.theme.RoyalGold
import com.Zudxofi5.Toltoq.core.theme.StageVoid
import com.Zudxofi5.Toltoq.core.theme.SurfaceBrass
import com.Zudxofi5.Toltoq.core.theme.SurfaceVelvet
import com.Zudxofi5.Toltoq.core.theme.TextSecondary
import com.Zudxofi5.Toltoq.domain.model.RunOutcome
import com.Zudxofi5.Toltoq.domain.model.RunResult
import com.Zudxofi5.Toltoq.core.ui.PrimaryButton
import com.Zudxofi5.Toltoq.core.ui.SecondaryButton
import com.Zudxofi5.Toltoq.core.ui.StarStrip
import com.Zudxofi5.Toltoq.core.ui.StatCard

@Composable
fun ResultRoute(
    run: RunResult,
    onPlayAgain: () -> Unit,
    onNextLevel: (Int) -> Unit,
    onLevels: () -> Unit,
    onMenu: () -> Unit
) {
    val resultViewModel: ResultViewModel = viewModel(factory = AppViewModelFactory())
    val state by resultViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(run.levelId) {
        resultViewModel.bind(run.levelId)
    }
    ResultScreen(
        run = run,
        state = state,
        onPlayAgain = onPlayAgain,
        onNextLevel = onNextLevel,
        onLevels = onLevels,
        onMenu = onMenu
    )
}

@Composable
private fun ResultScreen(
    run: RunResult,
    state: ResultUiState,
    onPlayAgain: () -> Unit,
    onNextLevel: (Int) -> Unit,
    onLevels: () -> Unit,
    onMenu: () -> Unit
) {
    val escaped = run.outcome == RunOutcome.ESCAPED
    val banner = when (run.outcome) {
        RunOutcome.ESCAPED -> stringResource(R.string.result_win_title)
        RunOutcome.SPOTTED -> stringResource(R.string.result_spotted_title)
        RunOutcome.OUT_OF_MOVES -> stringResource(R.string.result_moves_title)
    }
    val subline = when (run.outcome) {
        RunOutcome.ESCAPED -> stringResource(R.string.result_win_line)
        RunOutcome.SPOTTED -> stringResource(R.string.result_spotted_line)
        RunOutcome.OUT_OF_MOVES -> stringResource(R.string.result_moves_line)
    }
    val gradient = if (escaped) {
        Brush.verticalGradient(listOf(SurfaceBrass, SurfaceVelvet, StageVoid))
    } else {
        Brush.verticalGradient(listOf(DangerRed.copy(alpha = 0.22f), SurfaceVelvet, StageVoid))
    }

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
                .alpha(0.16f)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .alpha(0.9f)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(84.dp)
                    .alpha(if (escaped) 0.9f else 0.35f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = banner,
                style = MaterialTheme.typography.displaySmall,
                color = if (escaped) GoldLight else DangerRed,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = subline,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            if (escaped) {
                Spacer(modifier = Modifier.height(16.dp))
                StarStrip(earned = run.stars, total = 3, glyphSize = 34)
            }
            if (run.newBest && run.score > 0) {
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = stringResource(R.string.result_new_best),
                    style = MaterialTheme.typography.labelSmall,
                    color = StageVoid,
                    modifier = Modifier
                        .background(RoyalGold, RoundedCornerShape(8.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }
            if (escaped) {
                Spacer(modifier = Modifier.height(18.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        label = stringResource(R.string.stat_score),
                        value = run.score.toString(),
                        accent = RoyalGold,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = stringResource(R.string.stat_costume),
                        value = stringResource(R.string.fraction, run.pieces, 3),
                        accent = GoldLight,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = stringResource(R.string.stat_moves_left),
                        value = run.movesLeft.toString(),
                        accent = GoldLight,
                        modifier = Modifier.weight(1f)
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(18.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceVelvet, RoundedCornerShape(14.dp))
                        .border(1.dp, RoyalGold.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.result_best_score, state.bestScore),
                        style = MaterialTheme.typography.labelLarge,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(modifier = Modifier.height(26.dp))
            PrimaryButton(
                label = stringResource(R.string.cta_play_again),
                onClick = onPlayAgain,
                modifier = Modifier.fillMaxWidth()
            )
            if (escaped && state.hasNextLevel) {
                Spacer(modifier = Modifier.height(10.dp))
                SecondaryButton(
                    label = stringResource(R.string.cta_next_act),
                    onClick = { onNextLevel(state.nextLevelId) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SecondaryButton(
                    label = stringResource(R.string.cta_levels),
                    onClick = onLevels,
                    modifier = Modifier.weight(1f)
                )
                SecondaryButton(
                    label = stringResource(R.string.cta_menu),
                    onClick = onMenu,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
