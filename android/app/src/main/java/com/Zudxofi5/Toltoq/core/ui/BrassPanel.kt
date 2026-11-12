package com.Zudxofi5.Toltoq.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.Zudxofi5.Toltoq.core.theme.RoyalGold
import com.Zudxofi5.Toltoq.core.theme.SurfaceVelvet

@Composable
fun BrassPanel(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    showGrabRule: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .background(SurfaceVelvet.copy(alpha = 0.95f), shape)
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    listOf(
                        RoyalGold.copy(alpha = 0f),
                        RoyalGold.copy(alpha = 0.47f),
                        RoyalGold.copy(alpha = 0f)
                    )
                ),
                shape = shape
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showGrabRule) {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .width(44.dp)
                    .height(4.dp)
                    .background(RoyalGold.copy(alpha = 0.55f), RoundedCornerShape(2.dp))
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = content
        )
    }
}
