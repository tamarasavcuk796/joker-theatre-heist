package com.Zudxofi5.Toltoq.presentation.collection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.Zudxofi5.Toltoq.core.theme.SilhouetteTint
import com.Zudxofi5.Toltoq.core.theme.StageVoid
import com.Zudxofi5.Toltoq.core.theme.SurfaceBrass
import com.Zudxofi5.Toltoq.core.theme.SurfacePlaque
import com.Zudxofi5.Toltoq.core.theme.SurfaceVelvet
import com.Zudxofi5.Toltoq.core.theme.TextSecondary
import com.Zudxofi5.Toltoq.core.ui.EmptyStateMessage
import com.Zudxofi5.Toltoq.core.ui.HeaderBadge
import com.Zudxofi5.Toltoq.core.ui.ScreenHeader
import com.Zudxofi5.Toltoq.core.ui.SecondaryButton
import com.Zudxofi5.Toltoq.domain.model.CostumeProgress

@Composable
fun CollectionRoute(onMenu: () -> Unit) {
    val collectionViewModel: CollectionViewModel = viewModel(factory = AppViewModelFactory())
    val state by collectionViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        collectionViewModel.refresh()
    }
    CollectionScreen(
        state = state,
        onEquip = collectionViewModel::equip,
        onMenu = onMenu
    )
}

@Composable
private fun CollectionScreen(
    state: CollectionUiState,
    onEquip: (Int) -> Unit,
    onMenu: () -> Unit
) {
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
                title = stringResource(R.string.title_collection),
                onBack = onMenu,
                trailing = {
                    HeaderBadge(
                        label = stringResource(R.string.fraction, state.pieces, state.maxPieces)
                    )
                }
            )
            if (state.pieces <= 0) {
                EmptyStateMessage(
                    message = stringResource(R.string.collection_empty),
                    artwork = painterResource(R.drawable.sprite_4),
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.costumes, key = { it.costume.id }) { entry ->
                        CostumeCard(entry = entry, onEquip = onEquip)
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
private fun CostumeCard(entry: CostumeProgress, onEquip: (Int) -> Unit) {
    val ownedLabel = stringResource(R.string.state_owned)
    val lockedLabel = stringResource(R.string.state_locked)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfacePlaque, RoundedCornerShape(14.dp))
            .border(
                width = 1.dp,
                color = if (entry.complete) RoyalGold.copy(alpha = 0.5f) else RoyalGold.copy(alpha = 0.18f),
                shape = RoundedCornerShape(14.dp)
            )
            .semantics { stateDescription = if (entry.complete) ownedLabel else lockedLabel }
            .padding(horizontal = 12.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.sprite_4),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            colorFilter = if (entry.complete) null else ColorFilter.tint(SilhouetteTint),
            modifier = Modifier.size(56.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(entry.costume.nameRes),
            style = MaterialTheme.typography.labelSmall,
            color = GoldLight,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.collection_pieces, entry.pieces, entry.required),
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(6.dp))
        PieceRail(filled = entry.pieces, total = entry.required)
        if (entry.complete) {
            Spacer(modifier = Modifier.height(10.dp))
            SecondaryButton(
                label = if (entry.equipped) {
                    stringResource(R.string.cta_equipped)
                } else {
                    stringResource(R.string.cta_equip)
                },
                onClick = { onEquip(entry.costume.id) },
                enabled = !entry.equipped,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun PieceRail(filled: Int, total: Int) {
    val ratio = if (total <= 0) 0f else filled.toFloat() / total.toFloat()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .background(SurfaceVelvet, RoundedCornerShape(2.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(ratio)
                .height(4.dp)
                .background(RoyalGold, RoundedCornerShape(2.dp))
        )
    }
}
