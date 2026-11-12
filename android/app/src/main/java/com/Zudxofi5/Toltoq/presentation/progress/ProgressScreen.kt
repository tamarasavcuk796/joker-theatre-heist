package com.Zudxofi5.Toltoq.presentation.progress

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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Zudxofi5.Toltoq.R
import com.Zudxofi5.Toltoq.core.di.AppViewModelFactory
import com.Zudxofi5.Toltoq.core.theme.GoldLight
import com.Zudxofi5.Toltoq.core.theme.RoyalGold
import com.Zudxofi5.Toltoq.core.theme.StageVoid
import com.Zudxofi5.Toltoq.core.theme.SurfaceBrass
import com.Zudxofi5.Toltoq.core.theme.SurfacePlaque
import com.Zudxofi5.Toltoq.core.theme.SurfaceVelvet
import com.Zudxofi5.Toltoq.core.theme.TextSecondary
import com.Zudxofi5.Toltoq.core.ui.EmptyStateMessage
import com.Zudxofi5.Toltoq.core.ui.ScreenHeader
import com.Zudxofi5.Toltoq.core.ui.SectionTitle
import com.Zudxofi5.Toltoq.core.ui.SecondaryButton
import com.Zudxofi5.Toltoq.core.ui.StarStrip
import com.Zudxofi5.Toltoq.core.ui.StatCard
import com.Zudxofi5.Toltoq.domain.model.ActProgress

@Composable
fun ProgressRoute(onMenu: () -> Unit) {
    val progressViewModel: ProgressViewModel = viewModel(factory = AppViewModelFactory())
    val state by progressViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        progressViewModel.refresh()
    }
    ProgressScreen(state = state, onMenu = onMenu)
}

@Composable
private fun ProgressScreen(state: ProgressUiState, onMenu: () -> Unit) {
    val career = state.career
    val actNames = listOf(
        stringResource(R.string.act_one),
        stringResource(R.string.act_two),
        stringResource(R.string.act_three)
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(StageVoid, SurfaceVelvet, SurfaceBrass)))
    ) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.10f)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            ScreenHeader(
                title = stringResource(R.string.title_progress),
                onBack = onMenu
            )
            if (career == null || career.levelsCleared <= 0) {
                EmptyStateMessage(
                    message = stringResource(R.string.progress_empty),
                    artwork = painterResource(R.drawable.logo),
                    modifier = Modifier.weight(1f)
                )
            } else {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCard(
                            label = stringResource(R.string.stat_total_score),
                            value = career.totalScore.toString(),
                            accent = RoyalGold,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = stringResource(R.string.stat_stars),
                            value = stringResource(
                                R.string.fraction,
                                career.stars,
                                career.maxStars
                            ),
                            accent = GoldLight,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCard(
                            label = stringResource(R.string.stat_levels_cleared),
                            value = stringResource(
                                R.string.fraction,
                                career.levelsCleared,
                                career.totalLevels
                            ),
                            accent = GoldLight,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = stringResource(R.string.stat_best_escape),
                            value = career.bestEscapeMoves.toString(),
                            accent = RoyalGold,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(22.dp))
                    SectionTitle(label = stringResource(R.string.progress_acts))
                    Spacer(modifier = Modifier.height(12.dp))
                    for (act in state.acts) {
                        ActRow(
                            act = act,
                            name = actNames[(act.act - 1).coerceIn(0, 2)]
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SurfacePlaque, RoundedCornerShape(12.dp))
                            .border(1.dp, RoyalGold.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.progress_clean_runs, career.cleanRuns),
                            style = MaterialTheme.typography.labelLarge,
                            color = TextSecondary
                        )
                    }
                }
            }
            SecondaryButton(
                label = stringResource(R.string.cta_menu),
                onClick = onMenu,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            )
        }
    }
}

@Composable
private fun ActRow(act: ActProgress, name: String) {
    val ratio = if (act.maxStars <= 0) 0f else act.stars.toFloat() / act.maxStars.toFloat()
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.labelLarge,
                color = GoldLight
            )
            StarStrip(earned = if (act.stars > 3) 3 else act.stars, total = 3, glyphSize = 14)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .background(SurfaceVelvet, RoundedCornerShape(3.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(ratio)
                    .height(6.dp)
                    .background(RoyalGold, RoundedCornerShape(3.dp))
            )
        }
    }
}
