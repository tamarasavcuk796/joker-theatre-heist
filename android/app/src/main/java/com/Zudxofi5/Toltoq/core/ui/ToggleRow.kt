package com.Zudxofi5.Toltoq.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.Zudxofi5.Toltoq.R
import com.Zudxofi5.Toltoq.core.theme.DisabledFill
import com.Zudxofi5.Toltoq.core.theme.GoldLight
import com.Zudxofi5.Toltoq.core.theme.RoyalGold
import com.Zudxofi5.Toltoq.core.theme.StageVoid
import com.Zudxofi5.Toltoq.core.theme.SurfacePlaque
import com.Zudxofi5.Toltoq.core.theme.TextMuted
import com.Zudxofi5.Toltoq.core.theme.TextSecondary

@Composable
fun ToggleRow(
    label: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val state = if (checked) stringResource(R.string.state_on) else stringResource(R.string.state_off)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .background(SurfacePlaque, RoundedCornerShape(12.dp))
            .border(1.dp, RoyalGold.copy(alpha = 0.22f), RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .semantics { stateDescription = state },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.width(220.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = GoldLight
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = StageVoid,
                checkedTrackColor = RoyalGold,
                uncheckedThumbColor = TextMuted,
                uncheckedTrackColor = DisabledFill
            )
        )
    }
}
