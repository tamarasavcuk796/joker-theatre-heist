package com.Zudxofi5.Toltoq.presentation.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Zudxofi5.Toltoq.R
import com.Zudxofi5.Toltoq.core.theme.GoldLight
import com.Zudxofi5.Toltoq.core.theme.SurfaceVelvet
import com.Zudxofi5.Toltoq.core.theme.TextSecondary
import com.Zudxofi5.Toltoq.core.ui.PrimaryButton

@Composable
fun PauseDialog(
    movesLeft: Int,
    piecesHeld: Int,
    piecesTarget: Int,
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onMenu: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onResume,
        containerColor = SurfaceVelvet,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                text = stringResource(R.string.pause_title),
                style = MaterialTheme.typography.headlineMedium,
                color = GoldLight
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.pause_moves, movesLeft),
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary
                )
                Text(
                    text = stringResource(R.string.pause_pieces, piecesHeld, piecesTarget),
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary
                )
            }
        },
        confirmButton = {
            PrimaryButton(
                label = stringResource(R.string.cta_resume),
                onClick = onResume,
                height = 48
            )
        },
        dismissButton = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                TextButton(onClick = onRestart) {
                    Text(
                        text = stringResource(R.string.cta_restart),
                        style = MaterialTheme.typography.labelLarge,
                        color = GoldLight
                    )
                }
                TextButton(onClick = onMenu) {
                    Text(
                        text = stringResource(R.string.cta_menu),
                        style = MaterialTheme.typography.labelLarge,
                        color = GoldLight
                    )
                }
            }
        }
    )
}
