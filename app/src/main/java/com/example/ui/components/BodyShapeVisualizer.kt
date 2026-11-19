package com.example.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Female
import androidx.compose.material.icons.rounded.Male
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.i18n.LocalAppLanguageCode
import com.example.ui.i18n.strRes
import java.util.concurrent.ConcurrentHashMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.abs
import kotlin.math.roundToInt

const val BODY_FAT_MIN = 10f
const val BODY_FAT_MAX = 40f

fun formatBodyFatLabel(pct: Int, langCode: String): String {
    val clamped = pct.coerceIn(BODY_FAT_MIN.roundToInt(), BODY_FAT_MAX.roundToInt())
    val locale = java.util.Locale.forLanguageTag(com.example.ui.i18n.AppLanguage.fromCode(langCode).code)
    return java.text.NumberFormat.getPercentInstance(locale).apply {
        minimumFractionDigits = 0
        maximumFractionDigits = 0
    }.format(clamped / 100.0)
}

private data class BodyFatCategoryMeta(
    @get:StringRes val titleRes: Int,
    @get:StringRes val descRes: Int
)

private fun bodyFatMetaFor(pct: Int): BodyFatCategoryMeta = when {
    pct <= 15 -> BodyFatCategoryMeta(R.string.onb_body_fat_cat_athletic, R.string.onb_body_fat_desc_athletic)
    pct <= 22 -> BodyFatCategoryMeta(R.string.onb_body_fat_cat_fit, R.string.onb_body_fat_desc_fit)
    pct <= 28 -> BodyFatCategoryMeta(R.string.onb_body_fat_cat_average, R.string.onb_body_fat_desc_average)
    pct <= 34 -> BodyFatCategoryMeta(R.string.onb_body_fat_cat_curvy, R.string.onb_body_fat_desc_curvy)
    else -> BodyFatCategoryMeta(R.string.onb_body_fat_cat_high, R.string.onb_body_fat_desc_high)
}

@Composable
fun CurrentBodyShapeStep(
    bodyFatCurrent: Float,
    gender: String,
    onBodyFatChange: (Float) -> Unit
) {
    val langCode = LocalAppLanguageCode.current
    val pctInt = bodyFatCurrent.roundToInt().coerceIn(BODY_FAT_MIN.roundToInt(), BODY_FAT_MAX.roundToInt())
    val formattedPct = formatBodyFatLabel(pctInt, langCode)
    val meta = bodyFatMetaFor(pctInt)
    val primary = MaterialTheme.colorScheme.primary
    val surface = MaterialTheme.colorScheme.surface

    OnboardingStepTitle(
        title = strRes(R.string.onb_body_fat_current_title),
        subtitle = strRes(R.string.onb_body_fat_current_sub)
    )

    Card(
        colors = CardDefaults.cardColors(containerColor = surface),
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(1.2.dp, primary.copy(alpha = 0.30f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("onboarding_current_body_fat_card")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .drawWithCache {
                    val glow = Brush.radialGradient(
                        colors = listOf(primary.copy(alpha = 0.14f), Color.Transparent),
                        center = Offset(size.width * 0.5f, size.height * 0.48f),
                        radius = size.maxDimension * 0.65f
                    )
                    onDrawBehind { drawRect(glow) }
                }
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.background,
                        shape = CircleShape,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Icon(
                                imageVector = if (gender == "female") Icons.Rounded.Female else Icons.Rounded.Male,
                                contentDescription = null,
                                tint = primary,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = strRes(if (gender == "female") R.string.female_label else R.string.male_label),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Surface(
                        color = primary.copy(alpha = 0.14f),
                        shape = CircleShape
                    ) {
                        Text(
                            text = strRes(meta.titleRes),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = primary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                        )
                    }
                }

                AnatomicalBodyFigure(
                    bodyFatPct = bodyFatCurrent,
                    gender = gender,
                    isHighlighted = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(236.dp)
                        .testTag("current_body_figure_canvas")
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = strRes(R.string.onb_body_fat_estimated_label),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Surface(
                        color = primary,
                        shape = CircleShape
                    ) {
                        Text(
                            text = formattedPct,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                        )
                    }
                    Text(
                        text = strRes(meta.descRes),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }

    BodyFatMorphSlider(
        value = bodyFatCurrent,
        onValueChange = onBodyFatChange,
        sliderTestTag = "onboarding_body_fat_current_slider"
    )
}

@Composable
fun DesiredBodyShapeStep(
    bodyFatCurrent: Float,
    bodyFatTarget: Float,
    gender: String,
    onBodyFatTargetChange: (Float) -> Unit
) {
    val langCode = LocalAppLanguageCode.current
    val currentInt = bodyFatCurrent.roundToInt().coerceIn(BODY_FAT_MIN.roundToInt(), BODY_FAT_MAX.roundToInt())
    val targetInt = bodyFatTarget.roundToInt().coerceIn(BODY_FAT_MIN.roundToInt(), BODY_FAT_MAX.roundToInt())
    val currentLabel = formatBodyFatLabel(currentInt, langCode)
    val targetLabel = formatBodyFatLabel(targetInt, langCode)
    val currentMeta = bodyFatMetaFor(currentInt)
    val targetMeta = bodyFatMetaFor(targetInt)
    val delta = targetInt - currentInt

    val primary = MaterialTheme.colorScheme.primary
    val surface = MaterialTheme.colorScheme.surface

    val infiniteTransition = rememberInfiniteTransition(label = "body_transition_arrows")
    val arrowPulse by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "chevron_wave"
    )

    OnboardingStepTitle(
        title = strRes(R.string.onb_body_fat_target_title),
        subtitle = strRes(R.string.onb_body_fat_target_sub)
    )

    // Side-by-side comparison stage with animated transition arrows
    Card(
        colors = CardDefaults.cardColors(containerColor = surface),
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(1.2.dp, primary.copy(alpha = 0.30f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("onboarding_target_body_comparison_card")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 14.dp)
        ) {
            // Left: Current Body Shape
            Surface(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 10.dp)
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.14f),
                        shape = CircleShape
                    ) {
                        Text(
                            text = strRes(R.string.onb_body_shape_current_badge),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                        )
                    }

                    AnatomicalBodyFigure(
                        bodyFatPct = bodyFatCurrent,
                        gender = gender,
                        isHighlighted = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(176.dp)
                    )

                    Text(
                        text = currentLabel,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = strRes(currentMeta.titleRes),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Center: Transition Arrows + Delta Pill
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(horizontal = 6.dp)
            ) {
                Surface(
                    color = primary.copy(alpha = 0.14f),
                    shape = CircleShape,
                    border = BorderStroke(1.dp, primary.copy(alpha = 0.35f))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp)
                    ) {
                        for (idx in 0 until 3) {
                            val dist = kotlin.math.abs(arrowPulse - idx)
                            val alpha = (1f - (dist * 0.42f)).coerceIn(0.28f, 1f)
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                contentDescription = null,
                                tint = primary,
                                modifier = Modifier
                                    .size(16.dp)
                                    .alpha(alpha)
                            )
                        }
                    }
                }

                val deltaText = when {
                    delta < 0 -> strRes(R.string.onb_body_fat_delta_loss_fmt, kotlin.math.abs(delta))
                    delta > 0 -> strRes(R.string.onb_body_fat_delta_gain_fmt, delta)
                    else -> strRes(R.string.onb_body_fat_delta_keep)
                }
                Surface(
                    color = if (delta <= 0) primary else MaterialTheme.colorScheme.tertiary,
                    shape = CircleShape
                ) {
                    Text(
                        text = deltaText,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        maxLines = 1,
                        softWrap = false,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Right: Target Body Shape (live morphing)
            Surface(
                color = primary.copy(alpha = 0.08f),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.8.dp, primary),
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 10.dp)
                ) {
                    Surface(
                        color = primary,
                        shape = CircleShape
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.AutoAwesome,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = strRes(R.string.onb_body_shape_target_badge),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }

                    AnatomicalBodyFigure(
                        bodyFatPct = bodyFatTarget,
                        gender = gender,
                        isHighlighted = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(176.dp)
                            .testTag("target_body_figure_canvas")
                    )

                    Text(
                        text = targetLabel,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = primary
                    )
                    Text(
                        text = strRes(targetMeta.titleRes),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    // Quick preset chips for fast target selection
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        val presets = listOf(10, 15, 20, 25)
        presets.forEach { preset ->
            val selected = targetInt == preset
            OnboardingSelectableChip(
                label = formatBodyFatLabel(preset, langCode),
                selected = selected,
                onClick = { onBodyFatTargetChange(preset.toFloat()) },
                modifier = Modifier.weight(1f)
            )
        }
    }

    BodyFatMorphSlider(
        value = bodyFatTarget,
        onValueChange = onBodyFatTargetChange,
        sliderTestTag = "onboarding_body_fat_target_slider"
    )
}

@Composable
private fun BodyFatMorphSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    sliderTestTag: String
) {
    val langCode = LocalAppLanguageCode.current
    val primary = MaterialTheme.colorScheme.primary
    val ticks = listOf(10, 15, 20, 25, 30, 35, 40)
    val clamped = value.roundToInt().toFloat().coerceIn(BODY_FAT_MIN, BODY_FAT_MAX)
    val stepCount = (BODY_FAT_MAX - BODY_FAT_MIN).roundToInt()

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Slider(
                value = clamped,
                onValueChange = { raw ->
                    onValueChange(raw.roundToInt().toFloat().coerceIn(BODY_FAT_MIN, BODY_FAT_MAX))
                },
                valueRange = BODY_FAT_MIN..BODY_FAT_MAX,
                steps = stepCount,
                colors = SliderDefaults.colors(
                    thumbColor = primary,
                    activeTrackColor = primary,
                    inactiveTrackColor = primary.copy(alpha = 0.20f),
                    activeTickColor = Color.Transparent,
                    inactiveTickColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(sliderTestTag)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                ticks.forEach { tick ->
                    val active = kotlin.math.abs(clamped.roundToInt() - tick) <= 2
                    Text(
                        text = formatBodyFatLabel(tick, langCode),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (active) FontWeight.ExtraBold else FontWeight.Medium,
                        color = if (active) primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { onValueChange(tick.toFloat()) }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

/**
 * Parametric anatomical fitness character with FIXED skeletal frame anchors (cranium, clavicles,
 * hip-joint axis, wrists, ankles) while muscle definition (<15%) and localized adipose volume
 * (protruding belly, waist thickness, love handles, and gender-specific hip/thigh fullness
 * up to >40%) morph realistically with the slider.
 */
@Composable
fun AnatomicalBodyFigure(
    bodyFatPct: Float,
    gender: String,
    isHighlighted: Boolean,
    modifier: Modifier = Modifier
) {
    val clampedPct = bodyFatPct.coerceIn(BODY_FAT_MIN, BODY_FAT_MAX)
    val rawFatFactor = ((clampedPct - BODY_FAT_MIN) / (BODY_FAT_MAX - BODY_FAT_MIN)).coerceIn(0f, 1f)
    val animatedPct by animateFloatAsState(
        targetValue = clampedPct,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "body_fat_pct_anim"
    )
    val fatFactor by animateFloatAsState(
        targetValue = rawFatFactor,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "body_fat_morph"
    )
    val isFemale = gender.equals("female", ignoreCase = true)
    if (isFemale) {
        FemaleStudioAthleteFigure(
            bodyFatPct = animatedPct,
            fatFactor = fatFactor,
            isHighlighted = isHighlighted,
            modifier = modifier
        )
    } else {
        MaleStudioAthleteFigure(
            bodyFatPct = animatedPct,
            isHighlighted = isHighlighted,
            modifier = modifier
        )
    }
}

private val MALE_TIER_DRAWABLES = listOf(
    R.drawable.img_onb_male_athlete, // 10% - 15%: Defined six-pack & sharp muscle cuts
    R.drawable.img_onb_male_fit,     // 16% - 22%: Normal/athletic balanced, NO six-pack
    R.drawable.img_onb_male_average, // 23% - 31%: Fuller soft midsection, NO abs
    R.drawable.img_onb_male_heavy    // 32% - 40%: High body fat belly & chest, NO abs
)

@Composable
private fun MaleStudioAthleteFigure(
    bodyFatPct: Float,
    isHighlighted: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Pre-warm all 4 male cutouts into cache so tier transitions are instant with zero flash
    LaunchedEffect(context) {
        withContext(Dispatchers.Default) {
            MALE_TIER_DRAWABLES.forEach { loadTransparentAthleteBitmap(context, it) }
        }
    }

    val pctInt = bodyFatPct.roundToInt().coerceIn(BODY_FAT_MIN.roundToInt(), BODY_FAT_MAX.roundToInt())
    val rawFatFactor = ((bodyFatPct - BODY_FAT_MIN) / (BODY_FAT_MAX - BODY_FAT_MIN)).coerceIn(0f, 1f)
    val expansionCurve = rawFatFactor * rawFatFactor * (3f - 2f * rawFatFactor)

    val (tierResId, baseWaistMorph) = remember(bodyFatPct, pctInt) {
        when {
            // 10% - 15%: Lean & Defined (only range showing visible six-pack abs)
            pctInt <= 15 -> {
                val t = ((bodyFatPct - 10f) / 5f).coerceIn(0f, 1f)
                R.drawable.img_onb_male_athlete to (0.003f + 0.008f * t)
            }
            // 16% - 22%: Normal athletic-fit midsection, smooth stomach WITHOUT six-pack
            pctInt <= 22 -> {
                val t = ((bodyFatPct - 16f) / 6f).coerceIn(0f, 1f)
                R.drawable.img_onb_male_fit to (0.01f + 0.012f * t)
            }
            // 23% - 31%: Softer waist & abdominal fullness, ZERO abs definition
            pctInt <= 31 -> {
                val t = ((bodyFatPct - 23f) / 8f).coerceIn(0f, 1f)
                R.drawable.img_onb_male_average to (0.02f + 0.015f * t)
            }
            // 32% - 40%: High body fat fullness
            else -> {
                val t = ((bodyFatPct - 32f) / 8f).coerceIn(0f, 1f)
                R.drawable.img_onb_male_heavy to (0.035f + 0.015f * t)
            }
        }
    }

    val waistMorph = baseWaistMorph * (0.4f + 0.6f * expansionCurve)

    val activeBmp = remember(context, tierResId) { loadTransparentAthleteBitmap(context, tierResId) }

    val primary = MaterialTheme.colorScheme.primary
    val glowColor = if (isHighlighted) {
        primary.copy(alpha = 0.28f)
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.12f)
    }

    // Consistent full color/lighting paint without double-exposure alpha bleed or grayscale shifts
    val bitmapPaint = remember(isHighlighted) {
        android.graphics.Paint().apply {
            isFilterBitmap = true
            isAntiAlias = true
            colorFilter = null
            alpha = if (isHighlighted) 255 else 232
        }
    }
    val meshCols = 16
    val meshRows = 28
    val verts = remember { FloatArray((meshCols + 1) * (meshRows + 1) * 2) }

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w * 0.5f

        drawOval(
            brush = Brush.radialGradient(
                colors = listOf(glowColor, Color.Transparent),
                center = Offset(cx, h * 0.94f),
                radius = w * 0.34f
            ),
            topLeft = Offset(cx - w * 0.32f, h * 0.89f),
            size = Size(w * 0.64f, h * 0.09f)
        )

        val bmp = activeBmp ?: return@Canvas
        val bmpAspect = bmp.width.toFloat() / bmp.height.toFloat().coerceAtLeast(1f)
        val availH = h * 0.95f
        val availW = w * 0.84f
        val drawH: Float
        val drawW: Float
        if (availW / availH < bmpAspect) {
            drawW = availW
            drawH = drawW / bmpAspect
        } else {
            drawH = availH
            drawW = drawH * bmpAspect
        }
        val destLeft = cx - drawW * 0.5f
        val destTop = h * 0.945f - drawH

        // Proportional anatomical scaling transformation: head, torso, arms, and legs scale harmoniously
        val torsoHalfU = 0.16f
        val armOuterU = 0.36f
        val bellyDropPx = drawH * 0.009f * expansionCurve
        var idx = 0
        for (row in 0..meshRows) {
            val v = row / meshRows.toFloat()
            val torsoW = maleTorsoWeight(v)
            val armW = maleArmWeight(v)
            val legW = maleLegWeight(v)
            val headW = maleHeadAndNeckWeight(v)
            val baseShift = drawW * waistMorph

            for (col in 0..meshCols) {
                val u = col / meshCols.toFloat()
                val baseX = destLeft + u * drawW
                val baseY = destTop + v * drawH
                val centeredU = u - 0.5f
                val absU = abs(centeredU)
                val sign = if (centeredU < 0f) -1f else if (centeredU > 0f) 1f else 0f

                val dx = when {
                    // Head & Neck region: delicate proportional fullness in jawline & traps
                    v <= 0.24f -> {
                        val headExpand = drawW * 0.006f * expansionCurve * headW
                        sign * headExpand
                    }
                    // Core Torso / Waist / Abdomen / Pelvis
                    absU <= torsoHalfU -> {
                        val midShift = sign * (absU / torsoHalfU) * (baseShift * torsoW)
                        val legShift = sign * (absU / torsoHalfU) * (drawW * 0.012f * expansionCurve * legW)
                        midShift + legShift
                    }
                    // Outer Torso / Arms / Outer Thighs
                    absU <= armOuterU -> {
                        val armExpand = drawW * 0.014f * expansionCurve * armW
                        val outerLegExpand = drawW * 0.015f * expansionCurve * legW
                        sign * (baseShift * 0.14f + armExpand + outerLegExpand)
                    }
                    else -> {
                        val marginT = ((absU - armOuterU) / (0.5f - armOuterU)).coerceIn(0f, 1f)
                        val baseOuter = baseShift * 0.14f
                        sign * baseOuter * (1f - marginT)
                    }
                }

                // Natural adipose abdominal drape with body fat
                val coreYShift = if (absU < 0.13f && v in 0.40f..0.56f) {
                    val coreNorm = (1f - (absU / 0.13f)) * kotlin.math.sin(((v - 0.40f) / 0.16f) * Math.PI.toFloat())
                    bellyDropPx * coreNorm
                } else 0f

                verts[idx++] = baseX + dx
                verts[idx++] = baseY + coreYShift
            }
        }

        drawContext.canvas.nativeCanvas.drawBitmapMesh(
            bmp,
            meshCols,
            meshRows,
            verts,
            0,
            null,
            0,
            bitmapPaint
        )

        // Subcutaneous tissue softening: As body fat rises, diffuse sharp muscle grooves & add realistic fullness
        if (rawFatFactor > 0.16f) {
            val softFactor = ((rawFatFactor - 0.16f) / 0.84f).coerceIn(0f, 1f)
            val midY = destTop + drawH * 0.44f
            val domeRx = drawW * (0.12f + 0.04f * softFactor)
            val domeRy = drawH * (0.055f + 0.035f * softFactor)
            drawOval(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFE8BCA0).copy(alpha = softFactor * 0.22f),
                        Color(0xFFD49978).copy(alpha = softFactor * 0.08f),
                        Color.Transparent
                    ),
                    center = Offset(cx, midY),
                    radius = maxOf(domeRx, domeRy) * 1.25f
                ),
                topLeft = Offset(cx - domeRx, midY - domeRy),
                size = Size(domeRx * 2f, domeRy * 2f)
            )
        }
    }
}

private fun maleHeadAndNeckWeight(v: Float): Float = when {
    v < 0.08f || v > 0.24f -> 0f
    else -> {
        val t = (v - 0.08f) / 0.16f
        kotlin.math.sin(t * Math.PI.toFloat()).coerceIn(0f, 1f)
    }
}

private fun maleArmWeight(v: Float): Float = when {
    v < 0.22f || v > 0.58f -> 0f
    else -> {
        val t = (v - 0.22f) / 0.36f
        kotlin.math.sin(t * Math.PI.toFloat()).coerceIn(0f, 1f)
    }
}

private fun maleLegWeight(v: Float): Float = when {
    v < 0.54f || v > 0.86f -> 0f
    else -> {
        val t = (v - 0.54f) / 0.32f
        kotlin.math.sin(t * Math.PI.toFloat()).coerceIn(0f, 1f)
    }
}

private fun maleTorsoWeight(v: Float): Float = when {
    v <= 0.22f -> 0f
    v <= 0.34f -> ((v - 0.22f) / 0.12f).let { it * it * (3f - 2f * it) } * 0.45f
    v <= 0.54f -> {
        val t = (v - 0.34f) / 0.20f
        0.45f + 0.55f * (t * t * (3f - 2f * t))
    }
    v <= 0.68f -> {
        val t = (v - 0.54f) / 0.14f
        1.0f - 0.50f * (t * t * (3f - 2f * t))
    }
    else -> 0f
}

@Composable
private fun FemaleStudioAthleteFigure(
    bodyFatPct: Float,
    fatFactor: Float,
    isHighlighted: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val cutoutBitmap = remember(context) { loadTransparentFemaleAthleteBitmap(context) }
    val primary = MaterialTheme.colorScheme.primary
    val glowColor = if (isHighlighted) primary.copy(alpha = 0.28f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.12f)

    val tissueCurve = fatFactor * fatFactor * (3f - 2f * fatFactor)
    // Belly fullness curve begins only above ~22% body fat
    val bellyCurve = ((bodyFatPct - 22f) / (BODY_FAT_MAX - 22f)).coerceIn(0f, 1f).let {
        it * it * (3f - 2f * it)
    }
    // Lean abdominal definition ONLY below 16% body fat
    val leanMuscle = ((16f - bodyFatPct) / 6f).coerceIn(0f, 1f)

    val bitmapPaint = remember(isHighlighted) {
        android.graphics.Paint().apply {
            isFilterBitmap = true
            isAntiAlias = true
            colorFilter = null
            alpha = if (isHighlighted) 255 else 232
        }
    }

    val meshCols = 18
    val meshRows = 30
    val verts = remember { FloatArray((meshCols + 1) * (meshRows + 1) * 2) }

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w * 0.5f

        drawOval(
            brush = Brush.radialGradient(
                colors = listOf(glowColor, Color.Transparent),
                center = Offset(cx, h * 0.94f),
                radius = w * 0.34f
            ),
            topLeft = Offset(cx - w * 0.32f, h * 0.89f),
            size = Size(w * 0.64f, h * 0.09f)
        )

        val bmp = cutoutBitmap ?: return@Canvas
        val bmpAspect = bmp.width.toFloat() / bmp.height.toFloat().coerceAtLeast(1f)
        val availH = h * 0.95f
        val availW = w * 0.84f
        val drawH: Float
        val drawW: Float
        if (availW / availH < bmpAspect) {
            drawW = availW
            drawH = drawW / bmpAspect
        } else {
            drawH = availH
            drawW = drawH * bmpAspect
        }
        val destLeft = cx - drawW * 0.5f
        val destTop = h * 0.945f - drawH

        // Populate anatomical horizontal mesh warp: fixed head/shoulders/ankles while waist,
        // lower belly, hips, and upper thighs scale realistically with fatFactor.
        val torsoHalfU = 0.155f
        val armOuterU = 0.34f
        var idx = 0
        for (row in 0..meshRows) {
            val v = row / meshRows.toFloat()
            val verticalFatWeight = femaleVerticalFatWeight(v)
            val maxExpansionPx = drawW * 0.105f * tissueCurve * verticalFatWeight
            val bellyDropPx = drawH * 0.012f * bellyCurve * bellyDropWeight(v)

            for (col in 0..meshCols) {
                val u = col / meshCols.toFloat()
                val baseX = destLeft + u * drawW
                val baseY = destTop + v * drawH

                val centeredU = u - 0.5f
                val absU = abs(centeredU)
                val sign = if (centeredU < 0f) -1f else if (centeredU > 0f) 1f else 0f

                val dx = when {
                    absU <= torsoHalfU -> {
                        val norm = absU / torsoHalfU
                        sign * norm * maxExpansionPx
                    }
                    absU <= armOuterU -> {
                        // Shift arms cleanly outward to clear expanded waist/hips without stretching limbs
                        sign * maxExpansionPx * 0.88f
                    }
                    else -> {
                        // Smoothly return outer transparent margin toward frame edge
                        val marginT = ((absU - armOuterU) / (0.5f - armOuterU)).coerceIn(0f, 1f)
                        sign * maxExpansionPx * 0.88f * (1f - marginT)
                    }
                }

                val centerBellyYShift = if (absU < 0.12f) {
                    val coreT = 1f - (absU / 0.12f)
                    bellyDropPx * coreT
                } else 0f

                verts[idx++] = baseX + dx
                verts[idx++] = baseY + centerBellyYShift
            }
        }

        drawContext.canvas.nativeCanvas.drawBitmapMesh(
            bmp,
            meshCols,
            meshRows,
            verts,
            0,
            null,
            0,
            bitmapPaint
        )

        // Subtle volumetric shading to emphasize lean tone (<18%) vs fuller midsection (>26%..>40%)
        val bellyY = destTop + drawH * 0.425f
        if (leanMuscle > 0.28f) {
            val toneAlpha = (leanMuscle - 0.28f) * 0.18f
            drawLine(
                color = Color(0xFF9C6040).copy(alpha = toneAlpha),
                start = Offset(cx, destTop + drawH * 0.355f),
                end = Offset(cx, destTop + drawH * 0.435f),
                strokeWidth = 1.6f * (drawH / 220f),
                cap = StrokeCap.Round
            )
        }
        if (bellyCurve > 0.08f) {
            val domeRx = drawW * (0.095f + 0.055f * bellyCurve)
            val domeRy = drawH * (0.042f + 0.028f * bellyCurve)
            drawOval(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFF6C6A5).copy(alpha = bellyCurve * 0.24f),
                        Color(0xFFD68D67).copy(alpha = bellyCurve * 0.10f),
                        Color.Transparent
                    ),
                    center = Offset(cx, bellyY - domeRy * 0.15f),
                    radius = maxOf(domeRx, domeRy) * 1.15f
                ),
                topLeft = Offset(cx - domeRx, bellyY - domeRy * 0.6f),
                size = Size(domeRx * 2f, domeRy * 1.25f)
            )
            val foldArc = Path().apply {
                moveTo(cx - domeRx * 0.80f, bellyY + domeRy * 0.25f)
                quadraticTo(
                    cx,
                    bellyY + domeRy * 0.72f,
                    cx + domeRx * 0.80f,
                    bellyY + domeRy * 0.25f
                )
            }
            drawPath(
                path = foldArc,
                color = Color(0xFF9A593A).copy(alpha = (bellyCurve * 0.34f).coerceIn(0f, 0.34f)),
                style = Stroke(width = (1.3f + 0.9f * bellyCurve) * (drawH / 220f), cap = StrokeCap.Round)
            )
        }
    }
}

private fun femaleVerticalFatWeight(v: Float): Float = when {
    v <= 0.10f -> 0f // Cranium anchored
    v <= 0.21f -> ((v - 0.10f) / 0.11f).let { it * it * (3f - 2f * it) } * 0.12f // Gentle jawline & neck fullness
    v <= 0.27f -> 0.12f + ((v - 0.21f) / 0.06f).let { it * it * (3f - 2f * it) } * 0.20f // Upper ribcage ramp
    v <= 0.42f -> {
        // Waist to abdomen fullness peak
        val t = (v - 0.27f) / 0.15f
        0.32f + 0.68f * (t * t * (3f - 2f * t))
    }
    v <= 0.53f -> {
        // Lower belly & gynoid hips/upper thighs remain full
        val t = (v - 0.42f) / 0.11f
        1.00f - 0.14f * (t * t * (3f - 2f * t))
    }
    v <= 0.72f -> {
        // Mid-thigh tapers smoothly toward knees
        val t = (v - 0.53f) / 0.19f
        0.86f - 0.68f * (t * t * (3f - 2f * t))
    }
    v <= 0.88f -> {
        // Knees & calves taper cleanly to slender fixed ankles
        val t = (v - 0.72f) / 0.16f
        0.18f * (1f - (t * t * (3f - 2f * t)))
    }
    else -> 0f // Ankles & feet stay anchored
}

private fun bellyDropWeight(v: Float): Float = when {
    v < 0.34f || v > 0.54f -> 0f
    else -> {
        val norm = (v - 0.34f) / 0.20f
        kotlin.math.sin(norm * Math.PI).toFloat().coerceIn(0f, 1f)
    }
}

private val ATHLETE_CUTOUT_CACHE = ConcurrentHashMap<Int, Bitmap>()

private fun loadTransparentAthleteBitmap(context: Context, @DrawableRes resId: Int): Bitmap? {
    ATHLETE_CUTOUT_CACHE[resId]?.takeIf { !it.isRecycled }?.let { return it }

    val decoded = runCatching {
        BitmapFactory.decodeResource(context.resources, resId)
    }.getOrNull() ?: return null

    // Downscale to crisp working size for instant BFS corner flood-fill & smooth rendering
    val targetH = 680
    val bmp = if (decoded.height > targetH) {
        val targetW = (decoded.width * (targetH / decoded.height.toFloat())).roundToInt().coerceAtLeast(1)
        Bitmap.createScaledBitmap(decoded, targetW, targetH, true).also {
            if (it != decoded) decoded.recycle()
        }
    } else {
        decoded.copy(Bitmap.Config.ARGB_8888, true).also {
            if (it != decoded) decoded.recycle()
        }
    }

    val w = bmp.width
    val h = bmp.height
    val pixels = IntArray(w * h)
    bmp.getPixels(pixels, 0, w, 0, 0, w, h)

    fun isNearWhite(argb: Int): Boolean {
        val r = (argb ushr 16) and 0xFF
        val g = (argb ushr 8) and 0xFF
        val b = argb and 0xFF
        val maxC = maxOf(r, maxOf(g, b))
        val minC = minOf(r, minOf(g, b))
        return minC >= 228 && (maxC - minC) <= 22
    }

    val visited = BooleanArray(w * h)
    val queue = IntArray(w * h)
    var head = 0
    var tail = 0

    fun enqueueIfWhite(x: Int, y: Int) {
        val pos = y * w + x
        if (!visited[pos] && isNearWhite(pixels[pos])) {
            visited[pos] = true
            queue[tail++] = pos
        }
    }

    for (x in 0 until w) {
        enqueueIfWhite(x, 0)
        enqueueIfWhite(x, h - 1)
    }
    for (y in 0 until h) {
        enqueueIfWhite(0, y)
        enqueueIfWhite(w - 1, y)
    }

    while (head < tail) {
        val pos = queue[head++]
        pixels[pos] = pixels[pos] and 0x00FFFFFF
        val px = pos % w
        val py = pos / w
        if (px > 0) enqueueIfWhite(px - 1, py)
        if (px + 1 < w) enqueueIfWhite(px + 1, py)
        if (py > 0) enqueueIfWhite(px, py - 1)
        if (py + 1 < h) enqueueIfWhite(px, py + 1)
    }

    // Soft feather pass on immediate silhouette boundary pixels for smooth anti-aliased edges
    for (y in 1 until h - 1) {
        for (x in 1 until w - 1) {
            val pos = y * w + x
            if (!visited[pos]) {
                val adjBg = visited[pos - 1] || visited[pos + 1] || visited[pos - w] || visited[pos + w]
                if (adjBg) {
                    val argb = pixels[pos]
                    val r = (argb ushr 16) and 0xFF
                    val g = (argb ushr 8) and 0xFF
                    val b = argb and 0xFF
                    val lum = (r + g + b) / 3
                    if (lum > 195) {
                        val alpha = (((242 - lum) / 47f) * 255f).roundToInt().coerceIn(0, 255)
                        pixels[pos] = (alpha shl 24) or (argb and 0x00FFFFFF)
                    }
                }
            }
        }
    }

    val out = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
    out.setPixels(pixels, 0, w, 0, 0, w, h)
    if (bmp != out) bmp.recycle()
    ATHLETE_CUTOUT_CACHE.putIfAbsent(resId, out)
    return ATHLETE_CUTOUT_CACHE[resId] ?: out
}

private fun loadTransparentFemaleAthleteBitmap(context: Context): Bitmap? =
    loadTransparentAthleteBitmap(context, R.drawable.img_onb_female_athlete)
