package com.Zudxofi5.Toltoq.presentation.levelselect

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
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
import com.Zudxofi5.Toltoq.core.theme.SurfaceLocked
import com.Zudxofi5.Toltoq.core.theme.SurfacePlaque
import com.Zudxofi5.Toltoq.core.theme.SurfaceVelvet
import com.Zudxofi5.Toltoq.core.theme.TextMuted
import com.Zudxofi5.Toltoq.core.theme.TextSecondary
import com.Zudxofi5.Toltoq.core.ui.ChipRow
import com.Zudxofi5.Toltoq.core.ui.HeaderBadge
import com.Zudxofi5.Toltoq.core.ui.ScreenHeader
import com.Zudxofi5.Toltoq.core.ui.SecondaryButton
import com.Zudxofi5.Toltoq.core.ui.StarStrip

@Composable
fun LevelSelectRoute(onPlayLevel: (Int) -> Unit, onMenu: () -> Unit) {
    val levelSelectViewModel: LevelSelectViewModel = viewModel(factory = AppViewModelFactory())
    val state by levelSelectViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        levelSelectViewModel.refresh()
    }
    LevelSelectScreen(
        state = state,
        onSelectAct = levelSelectViewModel::selectAct,
        onPlayLevel = onPlayLevel,
        onMenu = onMenu
    )
}

@Composable
private fun LevelSelectScreen(
    state: LevelSelectUiState,
    onSelectAct: (Int) -> Unit,
    onPlayLevel: (Int) -> Unit,
    onMenu: () -> Unit
) {
    val actNames = listOf(
        stringResource(R.string.act_one),
        stringResource(R.string.act_two),
        stringResource(R.string.act_three)
    )
    val locked = state.acts.filter { !it.unlocked }.map { it.act - 1 }.toSet()
    val visible = state.plaques.filter { it.act == state.selectedAct }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(StageVoid, SurfaceVelvet, SurfaceBrass))
            )
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
                title = stringResource(R.string.title_level_select),
                onBack = onMenu,
                trailing = {
                    HeaderBadge(label = actNames[(state.selectedAct - 1).coerceIn(0, 2)])
                }
            )
            ChipRow(
                options = actNames,
                selectedIndex = state.selectedAct - 1,
                onSelect = { onSelectAct(it + 1) },
                lockedIndices = locked,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(14.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(visible, key = { it.levelId }) { plaque ->
                    LevelPlaqueCard(plaque = plaque, onPlayLevel = onPlayLevel)
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
private fun LevelPlaqueCard(plaque: LevelPlaque, onPlayLevel: (Int) -> Unit) {
    val lockedLabel = stringResource(R.string.state_locked)
    val openLabel = stringResource(R.string.state_unlocked)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(116.dp)
            .background(
                if (plaque.unlocked) SurfacePlaque else SurfaceLocked,
                RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = if (plaque.unlocked) RoyalGold.copy(alpha = 0.27f) else TextMuted.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(enabled = plaque.unlocked) { onPlayLevel(plaque.levelId) }
            .semantics { stateDescription = if (plaque.unlocked) openLabel else lockedLabel }
            .padding(horizontal = 10.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (plaque.unlocked) {
            Text(
                text = plaque.levelId.toString(),
                style = MaterialTheme.typography.headlineSmall,
                color = GoldLight
            )
            Spacer(modifier = Modifier.height(6.dp))
            StarStrip(earned = plaque.stars, total = 3, glyphSize = 15)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(plaque.nameRes),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        } else {
            Image(
                painter = painterResource(R.drawable.ic_lock),
                contentDescription = null,
                colorFilter = ColorFilter.tint(TextMuted),
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = lockedLabel,
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
        }
    }
}
