package com.Zudxofi5.Toltoq.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Zudxofi5.Toltoq.R
import com.Zudxofi5.Toltoq.core.theme.GoldLight
import com.Zudxofi5.Toltoq.core.theme.StarEmpty

@Composable
fun StarStrip(
    earned: Int,
    total: Int,
    glyphSize: Int,
    modifier: Modifier = Modifier
) {
    val glyph = stringResource(R.string.glyph_star)
    val description = stringResource(R.string.cd_stars, earned, total)
    Row(
        modifier = modifier.semantics { stateDescription = description },
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (index in 0 until total) {
            Text(
                text = glyph,
                style = TextStyle(fontSize = glyphSize.sp),
                color = if (index < earned) GoldLight else StarEmpty
            )
        }
    }
}
