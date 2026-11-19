package com.example.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.example.R
import kotlinx.coroutines.delay

enum class CoachAnimationMood {
    IDLE,
    COACHING,
    CHEERING
}

private val CalmBreathingEasing = CubicBezierEasing(0.42f, 0f, 0.58f, 1f)

/**
 * FitChicken mascot with feet firmly planted at bottom center (TransformOrigin(0.5f, 1f)),
 * zero horizontal or vertical positional displacement (translationX = 0f, translationY = 0f),
 * and a natural, continuous 3.0s ease-in-out chest breathing motion.
 */
@Composable
fun AnimatedKocTavuk(
    modifier: Modifier = Modifier,
    size: Dp = 128.dp,
    mood: CoachAnimationMood = CoachAnimationMood.IDLE,
    triggerKey: Any = Unit,
    loopDemo: Boolean = false,
    @Suppress("UNUSED_PARAMETER") showAura: Boolean = false,
    contentDescription: String = "FitChicken"
) {
    val defaultFrame = when (mood) {
        CoachAnimationMood.IDLE -> 0
        CoachAnimationMood.COACHING -> 1
        CoachAnimationMood.CHEERING -> 2
    }

    val isPreview = LocalInspectionMode.current
    val context = LocalContext.current
    val density = LocalDensity.current
    val imageLoader = remember(context) { AppImageLoader.get(context) }
    val targetPx = with(density) { size.roundToPx().coerceIn(96, 420) }

    var activeFrame by remember(mood) { mutableIntStateOf(defaultFrame) }
    var proudStretch by remember { mutableStateOf(false) }

    // Controlled, smooth posture stretch on tap/interaction without harsh bouncing or frame flashing
    LaunchedEffect(triggerKey, isPreview) {
        if (triggerKey != Unit && !loopDemo && !isPreview) {
            proudStretch = true
            delay(420L)
            proudStretch = false
        }
    }

    // Calm, deliberate posture cycle in demo mode with graceful pacing
    LaunchedEffect(loopDemo, mood, isPreview) {
        if (loopDemo && !isPreview) {
            val cycle = when (mood) {
                CoachAnimationMood.CHEERING -> listOf(2, 0)
                CoachAnimationMood.COACHING -> listOf(1, 0)
                CoachAnimationMood.IDLE -> listOf(0, 1)
            }
            var idx = 0
            while (true) {
                activeFrame = cycle[idx % cycle.size]
                idx++
                delay(3200L)
            }
        } else {
            activeFrame = defaultFrame
        }
    }

    // Continuous, calm breathing cycle with smooth sinusoidal chest expansion
    val breathProgress by rememberInfiniteTransition(label = "mascot_calm_breath").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = CalmBreathingEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breath_curve"
    )

    // Gentle, controlled posture flex when interacted with
    val interactionStretch by animateFloatAsState(
        targetValue = if (proudStretch) 1.018f else 1f,
        animationSpec = tween(durationMillis = 350, easing = CalmBreathingEasing),
        label = "interaction_stretch"
    )

    val frameRes = when (activeFrame) {
        1 -> R.drawable.img_koc_tavuk_1
        2 -> R.drawable.img_koc_tavuk_2
        else -> R.drawable.img_koc_tavuk_0
    }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier.size(size)
    ) {
        Crossfade(
            targetState = frameRes,
            animationSpec = tween(durationMillis = 420, easing = CalmBreathingEasing),
            label = "mascot_expression_crossfade"
        ) { visibleFrame ->
            val visibleRequest = remember(visibleFrame, targetPx) {
                ImageRequest.Builder(context)
                    .data(visibleFrame)
                    .size(targetPx, targetPx)
                    .precision(Precision.EXACT)
                    .crossfade(false)
                    .build()
            }
            AsyncImage(
                model = visibleRequest,
                imageLoader = imageLoader,
                contentDescription = contentDescription,
                alignment = Alignment.BottomCenter,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        // Feet strictly grounded at bottom center (pivot at feet base)
                        transformOrigin = TransformOrigin(0.5f, 1f)
                        // Upward chest & posture breathing without lifting feet or horizontal drift
                        scaleX = interactionStretch * (1f + 0.007f * breathProgress)
                        scaleY = interactionStretch * (1f + 0.018f * breathProgress)
                        translationX = 0f
                        translationY = 0f
                    }
            )
        }
    }
}
