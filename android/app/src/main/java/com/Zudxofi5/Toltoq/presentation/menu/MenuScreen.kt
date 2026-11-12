package com.Zudxofi5.Toltoq.presentation.menu

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Zudxofi5.Toltoq.R
import com.Zudxofi5.Toltoq.core.di.AppViewModelFactory
import com.Zudxofi5.Toltoq.core.theme.GoldLight
import com.Zudxofi5.Toltoq.core.theme.RoyalGold
import com.Zudxofi5.Toltoq.core.theme.StageVoid
import com.Zudxofi5.Toltoq.core.theme.SurfaceBrass
import com.Zudxofi5.Toltoq.core.theme.TextSecondary
import com.Zudxofi5.Toltoq.core.ui.BrassPanel
import com.Zudxofi5.Toltoq.core.ui.PrimaryButton
import com.Zudxofi5.Toltoq.core.ui.SecondaryButton
import com.Zudxofi5.Toltoq.core.ui.StatCard

@Composable
fun MenuRoute(
    onPlay: (Int) -> Unit,
    onLevels: () -> Unit,
    onCollection: () -> Unit,
    onProgress: () -> Unit,
    onSettings: () -> Unit
) {
    val menuViewModel: MenuViewModel = viewModel(factory = AppViewModelFactory())
    val state by menuViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        menuViewModel.refresh()
    }
    MenuScreen(
        state = state,
        onPlay = onPlay,
        onLevels = onLevels,
        onCollection = onCollection,
        onProgress = onProgress,
        onSettings = onSettings
    )
}

@Composable
private fun MenuScreen(
    state: MenuUiState,
    onPlay: (Int) -> Unit,
    onLevels: () -> Unit,
    onCollection: () -> Unit,
    onProgress: () -> Unit,
    onSettings: () -> Unit
) {
    val transition = rememberInfiniteTransition(label = "menuLoop")
    val sweep by transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(tween(6000, easing = LinearEasing), RepeatMode.Reverse),
        label = "sweep"
    )
    val float by transition.animateFloat(
        initialValue = 0f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(tween(3400, easing = LinearEasing), RepeatMode.Reverse),
        label = "float"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(StageVoid)
    ) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color.Transparent,
                            0.52f to StageVoid.copy(alpha = 0.8f),
                            0.72f to StageVoid
                        )
                    )
                )
        )
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .statusBarsPadding()
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = (sweep * 160).dp)
                        .size(220.dp)
                        .background(
                            Brush.radialGradient(
                                listOf(GoldLight.copy(alpha = 0.15f), Color.Transparent)
                            )
                        )
                )
                Image(
                    painter = painterResource(R.drawable.sprite_1),
                    contentDescription = stringResource(R.string.cd_performer),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 28.dp, bottom = 8.dp)
                        .offset(y = (-float).dp)
                        .size(132.dp)
                )
                Image(
                    painter = painterResource(R.drawable.sprite_5),
                    contentDescription = stringResource(R.string.cd_stage_door),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 24.dp, bottom = 10.dp)
                        .size(96.dp)
                        .alpha(0.85f)
                )
                PieceBadge(
                    pieces = state.pieces,
                    maxPieces = state.maxPieces,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                )
            }
            MenuSheet(
                state = state,
                onPlay = onPlay,
                onLevels = onLevels,
                onCollection = onCollection,
                onProgress = onProgress,
                onSettings = onSettings
            )
        }
    }
}

@Composable
private fun PieceBadge(pieces: Int, maxPieces: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(SurfaceBrass, RoundedCornerShape(12.dp))
            .border(1.dp, RoyalGold.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.sprite_4),
            contentDescription = stringResource(R.string.cd_costume_piece),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = stringResource(R.string.fraction, pieces, maxPieces),
            style = MaterialTheme.typography.labelLarge,
            color = GoldLight
        )
    }
}

@Composable
private fun MenuSheet(
    state: MenuUiState,
    onPlay: (Int) -> Unit,
    onLevels: () -> Unit,
    onCollection: () -> Unit,
    onProgress: () -> Unit,
    onSettings: () -> Unit
) {
    BrassPanel(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        showGrabRule = true
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 6.dp)
                    .size(120.dp)
                    .alpha(0.08f)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp)
                    .padding(top = 16.dp, bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.menu_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = GoldLight,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.menu_tagline),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
                if (state.levelsCleared > 0 && state.bestScore > 0) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCard(
                            label = stringResource(R.string.stat_acts_cleared),
                            value = stringResource(
                                R.string.fraction,
                                state.levelsCleared,
                                state.totalLevels
                            ),
                            accent = GoldLight,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = stringResource(R.string.stat_best_score),
                            value = state.bestScore.toString(),
                            accent = RoyalGold,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
                PrimaryButton(
                    label = stringResource(R.string.cta_play),
                    onClick = { onPlay(state.nextLevelId) },
                    modifier = Modifier.fillMaxWidth()
                )
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
                        label = stringResource(R.string.cta_collection),
                        onClick = onCollection,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TextButton(
                        onClick = onProgress,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.cta_progress),
                            style = MaterialTheme.typography.labelLarge,
                            color = GoldLight
                        )
                    }
                    TextButton(
                        onClick = onSettings,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.cta_settings),
                            style = MaterialTheme.typography.labelLarge,
                            color = GoldLight
                        )
                    }
                }
            }
        }
    }
}
