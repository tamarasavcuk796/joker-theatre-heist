package com.Zudxofi5.Toltoq.presentation.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Zudxofi5.Toltoq.R
import com.Zudxofi5.Toltoq.core.theme.RoyalGold
import com.Zudxofi5.Toltoq.core.theme.StageVoid
import com.Zudxofi5.Toltoq.core.theme.SurfaceVelvet
import com.Zudxofi5.Toltoq.core.ui.PrimaryButton
import com.Zudxofi5.Toltoq.core.ui.SecondaryButton

@Composable
fun ControlBar(
    bellsLeft: Int,
    enabled: Boolean,
    onBell: () -> Unit,
    onWait: () -> Unit,
    onRestart: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(18.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .shadow(16.dp, shape, ambientColor = RoyalGold, spotColor = StageVoid)
            .background(SurfaceVelvet, shape)
            .border(1.dp, RoyalGold.copy(alpha = 0.4f), shape)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PrimaryButton(
            label = stringResource(R.string.cta_bell, bellsLeft),
            onClick = onBell,
            enabled = enabled && bellsLeft > 0,
            height = 48,
            modifier = Modifier.weight(1.1f)
        )
        SecondaryButton(
            label = stringResource(R.string.cta_wait),
            onClick = onWait,
            enabled = enabled,
            modifier = Modifier.weight(1f)
        )
        SecondaryButton(
            label = stringResource(R.string.cta_restart),
            onClick = onRestart,
            modifier = Modifier.weight(1f)
        )
    }
}
