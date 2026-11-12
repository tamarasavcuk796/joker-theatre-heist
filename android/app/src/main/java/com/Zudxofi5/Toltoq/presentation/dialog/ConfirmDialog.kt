package com.Zudxofi5.Toltoq.presentation.dialog

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Zudxofi5.Toltoq.R
import com.Zudxofi5.Toltoq.core.theme.GoldLight
import com.Zudxofi5.Toltoq.core.theme.SurfaceVelvet
import com.Zudxofi5.Toltoq.core.theme.TextSecondary
import com.Zudxofi5.Toltoq.core.ui.PrimaryButton

@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    confirmLabel: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceVelvet,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = GoldLight
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )
        },
        confirmButton = {
            PrimaryButton(
                label = confirmLabel,
                onClick = onConfirm,
                height = 48
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.cta_cancel),
                    style = MaterialTheme.typography.labelLarge,
                    color = GoldLight
                )
            }
        }
    )
}
