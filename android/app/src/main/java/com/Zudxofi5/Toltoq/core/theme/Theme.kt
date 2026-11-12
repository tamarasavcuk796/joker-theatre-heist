package com.Zudxofi5.Toltoq.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val JokerColorScheme = darkColorScheme(
    primary = RoyalGold,
    onPrimary = StageVoid,
    primaryContainer = SurfaceBrass,
    onPrimaryContainer = GoldLight,
    secondary = GoldLight,
    onSecondary = StageVoid,
    secondaryContainer = SurfacePlaque,
    onSecondaryContainer = GoldLight,
    tertiary = AntiqueBrass,
    onTertiary = TextPrimary,
    background = StageVoid,
    onBackground = TextPrimary,
    surface = SurfaceVelvet,
    onSurface = TextPrimary,
    surfaceVariant = SurfacePlaque,
    onSurfaceVariant = TextSecondary,
    outline = AntiqueBrass,
    outlineVariant = BrassDeep,
    error = DangerRed,
    onError = StageVoid
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = JokerColorScheme,
        typography = AppTypography,
        content = content
    )
}
