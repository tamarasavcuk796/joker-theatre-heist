package com.Zudxofi5.Toltoq.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.Zudxofi5.Toltoq.R
import com.Zudxofi5.Toltoq.core.theme.GoldLight
import com.Zudxofi5.Toltoq.core.theme.RoyalGold
import com.Zudxofi5.Toltoq.core.theme.SurfacePlaque
import com.Zudxofi5.Toltoq.core.theme.TextMuted
import com.Zudxofi5.Toltoq.core.theme.TextSecondary

@Composable
fun ChipRow(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    lockedIndices: Set<Int> = emptySet()
) {
    val selectedState = stringResource(R.string.state_selected)
    val lockedState = stringResource(R.string.state_locked)
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEachIndexed { index, option ->
            val locked = lockedIndices.contains(index)
            val active = index == selectedIndex && !locked
            val description = when {
                locked -> lockedState
                active -> selectedState
                else -> option
            }
            Chip(
                label = option,
                active = active,
                locked = locked,
                description = description,
                onSelect = { onSelect(index) }
            )
        }
    }
}

@Composable
private fun RowScope.Chip(
    label: String,
    active: Boolean,
    locked: Boolean,
    description: String,
    onSelect: () -> Unit
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .background(
                if (active) RoyalGold.copy(alpha = 0.13f) else SurfacePlaque,
                RoundedCornerShape(10.dp)
            )
            .border(
                width = if (active) 1.5.dp else 1.dp,
                color = if (active) RoyalGold else RoyalGold.copy(alpha = 0.18f),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(enabled = !locked, onClick = onSelect)
            .semantics { stateDescription = description },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = when {
                locked -> TextMuted
                active -> GoldLight
                else -> TextSecondary
            },
            textAlign = TextAlign.Center
        )
    }
}
