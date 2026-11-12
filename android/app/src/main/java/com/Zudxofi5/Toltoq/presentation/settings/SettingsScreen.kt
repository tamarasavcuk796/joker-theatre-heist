package com.Zudxofi5.Toltoq.presentation.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ButtonDefaults
import com.Zudxofi5.Toltoq.R
import com.Zudxofi5.Toltoq.core.di.AppViewModelFactory
import com.Zudxofi5.Toltoq.core.theme.DangerRed
import com.Zudxofi5.Toltoq.core.theme.StageVoid
import com.Zudxofi5.Toltoq.core.theme.SurfaceBrass
import com.Zudxofi5.Toltoq.core.theme.SurfaceVelvet
import com.Zudxofi5.Toltoq.core.ui.ChipRow
import com.Zudxofi5.Toltoq.core.ui.ScreenHeader
import com.Zudxofi5.Toltoq.core.ui.SecondaryButton
import com.Zudxofi5.Toltoq.core.ui.SectionTitle
import com.Zudxofi5.Toltoq.core.ui.ToggleRow
import com.Zudxofi5.Toltoq.presentation.dialog.ConfirmDialog

@Composable
fun SettingsRoute(onMenu: () -> Unit) {
    val settingsViewModel: SettingsViewModel = viewModel(factory = AppViewModelFactory())
    val state by settingsViewModel.state.collectAsStateWithLifecycle()
    SettingsScreen(
        state = state,
        onRehearsal = settingsViewModel::setRehearsalIndex,
        onConeMode = settingsViewModel::setConeMode,
        onHighContrast = settingsViewModel::setHighContrast,
        onReducedMotion = settingsViewModel::setReducedMotion,
        onMoveHints = settingsViewModel::setMoveHints,
        onAskReset = settingsViewModel::askReset,
        onDismissReset = settingsViewModel::dismissReset,
        onConfirmReset = settingsViewModel::confirmReset,
        onMenu = onMenu
    )
}

@Composable
private fun SettingsScreen(
    state: SettingsUiState,
    onRehearsal: (Int) -> Unit,
    onConeMode: (Int) -> Unit,
    onHighContrast: (Boolean) -> Unit,
    onReducedMotion: (Boolean) -> Unit,
    onMoveHints: (Boolean) -> Unit,
    onAskReset: () -> Unit,
    onDismissReset: () -> Unit,
    onConfirmReset: () -> Unit,
    onMenu: () -> Unit
) {
    val rehearsalOptions = listOf(
        stringResource(R.string.rehearsal_short),
        stringResource(R.string.rehearsal_standard),
        stringResource(R.string.rehearsal_long)
    )
    val coneOptions = listOf(
        stringResource(R.string.cones_rehearsal_only),
        stringResource(R.string.cones_always)
    )
    val rehearsalIndex = SettingsViewModel.REHEARSAL_OPTIONS
        .indexOf(state.settings.rehearsalSeconds)
        .coerceAtLeast(0)

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
                title = stringResource(R.string.title_settings),
                onBack = onMenu
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {
                SectionTitle(label = stringResource(R.string.settings_rehearsal))
                Spacer(modifier = Modifier.height(10.dp))
                ChipRow(
                    options = rehearsalOptions,
                    selectedIndex = rehearsalIndex,
                    onSelect = onRehearsal
                )
                Spacer(modifier = Modifier.height(20.dp))
                SectionTitle(label = stringResource(R.string.settings_cones))
                Spacer(modifier = Modifier.height(10.dp))
                ChipRow(
                    options = coneOptions,
                    selectedIndex = if (state.settings.conesAlwaysVisible) 1 else 0,
                    onSelect = onConeMode
                )
                Spacer(modifier = Modifier.height(20.dp))
                SectionTitle(label = stringResource(R.string.settings_stage))
                Spacer(modifier = Modifier.height(10.dp))
                ToggleRow(
                    label = stringResource(R.string.settings_contrast),
                    description = stringResource(R.string.settings_contrast_hint),
                    checked = state.settings.highContrast,
                    onCheckedChange = onHighContrast
                )
                Spacer(modifier = Modifier.height(10.dp))
                ToggleRow(
                    label = stringResource(R.string.settings_motion),
                    description = stringResource(R.string.settings_motion_hint),
                    checked = state.settings.reducedMotion,
                    onCheckedChange = onReducedMotion
                )
                Spacer(modifier = Modifier.height(10.dp))
                ToggleRow(
                    label = stringResource(R.string.settings_hints),
                    description = stringResource(R.string.settings_hints_hint),
                    checked = state.settings.moveHints,
                    onCheckedChange = onMoveHints
                )
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedButton(
                    onClick = onAskReset,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DangerRed),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                ) {
                    Text(
                        text = stringResource(R.string.cta_reset_progress),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            SecondaryButton(
                label = stringResource(R.string.cta_menu),
                onClick = onMenu,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            )
        }
        if (state.confirmReset) {
            ConfirmDialog(
                title = stringResource(R.string.reset_title),
                message = stringResource(R.string.reset_message),
                confirmLabel = stringResource(R.string.cta_reset),
                onConfirm = onConfirmReset,
                onDismiss = onDismissReset
            )
        }
    }
}
