package com.example.ui.components

import android.content.Context
import android.os.Build
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.ImageRequest
import coil.size.Precision
import com.example.R
import com.example.data.catalog.ExerciseMediaCatalog
import com.example.data.model.ExerciseItem
import com.example.ui.i18n.strRes
import kotlinx.coroutines.delay

/** Shared Coil instance with bounded caches for local mascot assets and exercise photos. */
object AppImageLoader {
    @Volatile private var instance: ImageLoader? = null

    fun get(context: Context): ImageLoader = instance ?: synchronized(this) {
        val appCtx = context.applicationContext
        instance ?: ImageLoader.Builder(appCtx)
            .components {
                if (Build.VERSION.SDK_INT >= 28) add(ImageDecoderDecoder.Factory())
                else add(GifDecoder.Factory())
            }
            .memoryCache { MemoryCache.Builder(appCtx).maxSizePercent(0.18).build() }
            .diskCache {
                DiskCache.Builder()
                    .directory(appCtx.cacheDir.resolve("coil_image_cache"))
                    .maxSizeBytes(64L * 1024L * 1024L)
                    .build()
            }
            .allowHardware(true)
            .crossfade(250)
            .respectCacheHeaders(false)
            .build()
            .also { instance = it }
    }
}

@Composable
fun rememberGifEnabledImageLoader(): ImageLoader {
    val context = LocalContext.current
    return remember(context) { AppImageLoader.get(context) }
}

private val PositionBottomScrim = Brush.verticalGradient(
    colors = listOf(Color.Transparent, Color(0xB80E1117), Color(0xEC0B0D12))
)

@Composable
fun ExerciseAnimationPlayer(
    exercise: ExerciseItem,
    localizedName: String,
    localizedTargetMuscle: String,
    modifier: Modifier = Modifier,
    @Suppress("UNUSED_PARAMETER") gender: String = "male",
    isFullScreenHero: Boolean = false,
    playerHeight: Dp = if (isFullScreenHero) 236.dp else 196.dp
) {
    // Dataset photos are exercise-specific. Never substitute a different movement just because
    // it trains the same muscle; unavailable media gets a clear placeholder instead.
    val media = remember(exercise.id) { ExerciseMediaCatalog.exactMediaFor(exercise.id) }
    val startTitle = strRes(R.string.ex_pos_start_title)
    val peakTitle = strRes(R.string.ex_pos_peak_title)
    val demoLabel = strRes(R.string.ex_demo_label)

    var activeStep by remember(exercise.id) { mutableIntStateOf(1) }
    LaunchedEffect(exercise.id) {
        activeStep = 1
        while (true) {
            delay(1_650L)
            activeStep = if (activeStep == 1) 2 else 1
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier.fillMaxWidth().testTag("exercise_animation_player_${exercise.id}")
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Surface(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f),
                shape = CircleShape,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Icon(Icons.Rounded.FitnessCenter, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(5.dp))
                    Text(
                        localizedTargetMuscle,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = CircleShape,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Text(
                    demoLabel,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    maxLines = 1
                )
            }
        }

        BoxWithConstraints(Modifier.fillMaxWidth()) {
            val sideBySide = maxWidth >= 290.dp
            val cardHeight = if (sideBySide) playerHeight.coerceIn(188.dp, 236.dp) else 184.dp
            if (sideBySide) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    ExercisePositionCard(1, startTitle, media?.startFrameUrl, "$localizedName - $startTitle", activeStep == 1, Modifier.weight(1f).height(cardHeight).testTag("exercise_start_position_card"))
                    ExercisePositionCard(2, peakTitle, media?.peakFrameUrl, "$localizedName - $peakTitle", activeStep == 2, Modifier.weight(1f).height(cardHeight).testTag("exercise_peak_position_card"))
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    ExercisePositionCard(1, startTitle, media?.startFrameUrl, "$localizedName - $startTitle", activeStep == 1, Modifier.fillMaxWidth().height(cardHeight).testTag("exercise_start_position_card"))
                    ExercisePositionCard(2, peakTitle, media?.peakFrameUrl, "$localizedName - $peakTitle", activeStep == 2, Modifier.fillMaxWidth().height(cardHeight).testTag("exercise_peak_position_card"))
                }
            }
        }
    }
}

@Composable
private fun ExercisePositionCard(
    stepNumber: Int,
    phaseTitle: String,
    imageUrl: String?,
    contentDescription: String,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageLoader = rememberGifEnabledImageLoader()
    val primary = MaterialTheme.colorScheme.primary
    val cardScale by animateFloatAsState(
        targetValue = if (isActive) 1f else 0.982f,
        animationSpec = tween(520, easing = FastOutSlowInEasing),
        label = "exercise_position_scale"
    )
    val borderAlpha by animateFloatAsState(
        targetValue = if (isActive) 0.95f else 0.28f,
        animationSpec = tween(520, easing = FastOutSlowInEasing),
        label = "exercise_position_border"
    )
    val photoRequest = remember(imageUrl) {
        imageUrl?.let {
            ImageRequest.Builder(context)
                .data(it)
                .size(720, 560)
                .precision(Precision.INEXACT)
                .crossfade(250)
                .build()
        }
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF10131A)),
        border = BorderStroke(
            if (isActive) 2.dp else 1.dp,
            if (isActive) primary.copy(alpha = borderAlpha) else Color.White.copy(alpha = 0.18f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = modifier.graphicsLayer { scaleX = cardScale; scaleY = cardScale }
    ) {
        Box(Modifier.fillMaxSize().background(Color(0xFF11151D))) {
            // Keep a useful offline state behind Coil; if an image is absent or fails, no unrelated
            // exercise photo is shown in its place.
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.align(Alignment.Center).padding(16.dp)
            ) {
                Icon(Icons.Rounded.ImageNotSupported, contentDescription = null, tint = Color.White.copy(alpha = 0.68f), modifier = Modifier.size(28.dp))
                Text(
                    strRes(R.string.ex_demo_unavailable),
                    color = Color.White.copy(alpha = 0.76f),
                    style = MaterialTheme.typography.labelSmall
                )
            }
            if (photoRequest != null) {
                AsyncImage(
                    model = photoRequest,
                    imageLoader = imageLoader,
                    contentDescription = contentDescription,
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize().padding(7.dp).clip(RoundedCornerShape(15.dp))
                )
            }
            Box(
                Modifier.fillMaxWidth().height(58.dp).align(Alignment.BottomCenter).background(PositionBottomScrim)
            )
            Surface(
                color = if (isActive) primary else Color(0xFF151922).copy(alpha = 0.92f),
                shape = CircleShape,
                border = if (isActive) null else BorderStroke(1.dp, Color.White.copy(alpha = 0.20f)),
                modifier = Modifier.align(Alignment.TopStart).padding(10.dp)
            ) {
                Text(
                    text = "0$stepNumber",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isActive) MaterialTheme.colorScheme.onPrimary else Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
            Surface(
                color = Color(0xFF12161F).copy(alpha = 0.90f),
                shape = CircleShape,
                border = BorderStroke(1.dp, if (isActive) primary.copy(alpha = 0.72f) else Color.White.copy(alpha = 0.16f)),
                modifier = Modifier.align(Alignment.BottomStart).padding(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)) {
                    Box(Modifier.size(7.dp).clip(CircleShape).background(if (isActive) primary else Color.White.copy(alpha = 0.58f)))
                    Spacer(Modifier.width(6.dp))
                    Text(phaseTitle, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color.White, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}
