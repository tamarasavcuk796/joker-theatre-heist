package com.Zudxofi5.Toltoq.presentation.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Zudxofi5.Toltoq.R
import com.Zudxofi5.Toltoq.core.di.AppViewModelFactory
import com.Zudxofi5.Toltoq.core.theme.AntiqueBrass
import com.Zudxofi5.Toltoq.core.theme.GoldLight
import com.Zudxofi5.Toltoq.core.theme.RoyalGold
import com.Zudxofi5.Toltoq.core.theme.StageInk
import com.Zudxofi5.Toltoq.core.theme.StageVoid
import com.Zudxofi5.Toltoq.core.theme.SurfaceBrass
import com.Zudxofi5.Toltoq.core.theme.SurfacePlaque
import com.Zudxofi5.Toltoq.core.theme.TextMuted
import com.Zudxofi5.Toltoq.core.theme.TextSecondary

@Composable
fun SplashRoute(onFinished: () -> Unit) {
    val splashViewModel: SplashViewModel = viewModel(factory = AppViewModelFactory())
    val finished by splashViewModel.finished.collectAsStateWithLifecycle()
    LaunchedEffect(finished) {
        if (finished) onFinished()
    }
    SplashScreen()
}

@Composable
private fun SplashScreen() {
    val transition = rememberInfiniteTransition(label = "splashLoop")
    val ringAngle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(14000, easing = LinearEasing)),
        label = "ring"
    )
    val breath by transition.animateFloat(
        initialValue = 0.97f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            tween(2200, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "breath"
    )
    val drift by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(5200, easing = LinearEasing)),
        label = "drift"
    )
    val curtainTop = StageInk
    val curtainMid = StageVoid
    val curtainBottom = SurfacePlaque

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(StageVoid)
    ) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.22f)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(curtainTop, curtainMid, curtainBottom)))
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(SurfaceBrass.copy(alpha = 0.6f), Color.Transparent),
                        radius = 640f
                    )
                )
        )
        DustMotes(progress = drift)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.size(172.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .rotate(ringAngle)
                        .border(
                            width = 2.dp,
                            brush = Brush.sweepGradient(
                                listOf(AntiqueBrass, GoldLight, AntiqueBrass)
                            ),
                            shape = CircleShape
                        )
                )
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.cd_brand_crest),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(132.dp)
                        .scale(breath)
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = stringResource(R.string.splash_title),
                style = MaterialTheme.typography.displaySmall,
                color = GoldLight,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(16.dp))
            BrassRule()
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.splash_subtitle),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            BrassRule()
            Spacer(modifier = Modifier.height(46.dp))
            LinearProgressIndicator(
                color = RoyalGold,
                trackColor = SurfacePlaque,
                strokeCap = StrokeCap.Round,
                modifier = Modifier
                    .width(176.dp)
                    .height(4.dp)
                    .background(SurfacePlaque, RoundedCornerShape(2.dp))
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.splash_loading),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
        }
    }
}

@Composable
private fun BrassRule() {
    Box(
        modifier = Modifier
            .width(96.dp)
            .height(1.dp)
            .background(AntiqueBrass)
    )
}

@Composable
private fun DustMotes(progress: Float) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        for (index in 0 until 6) {
            val seed = (index + 1) * 0.137f
            val x = size.width * ((seed * 7f) % 1f)
            val span = (progress + seed) % 1f
            val y = size.height * (1f - span)
            val radius = (2f + (index % 3) * 1.2f) * density
            drawCircle(
                color = GoldLight.copy(alpha = 0.08f + 0.14f * (1f - span)),
                radius = radius,
                center = Offset(x, y)
            )
        }
    }
}
