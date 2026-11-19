package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.MonitorWeight
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.catalog.localizedTitle
import com.example.data.model.ExerciseItem
import com.example.data.model.WorkoutProgram
import com.example.ui.components.AnimatedKocTavuk
import com.example.ui.components.CoachAnimationMood
import com.example.ui.components.DailyQuestsAndChallengeCard
import com.example.ui.components.DailyStreakAndLevelCard
import com.example.ui.components.DailyWorkoutChecklistCard
import com.example.ui.components.DailyXpProWheelCard
import com.example.ui.components.ExerciseDetailBottomSheet
import com.example.ui.components.InviteFriendsProCard
import com.example.ui.components.RankAndXpHeroCard
import com.example.ui.components.RankProgressSheet
import com.example.ui.components.formatBodyFatLabel
import com.example.ui.i18n.LocalAppLanguageCode
import com.example.ui.i18n.LocalAppLocale
import com.example.ui.i18n.strRes
import com.example.ui.viewmodel.MainUiState
import com.example.ui.viewmodel.WheelSpinOutcome
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: MainUiState,
    personalizedWorkouts: List<WorkoutProgram>,
    isWorkoutAccessible: (WorkoutProgram) -> Boolean,
    adsWatchedForWorkout: (String) -> Int,
    onTapChicken: () -> Unit,
    onClaimDailyCheckIn: () -> Unit,
    onCompleteQuest: (String) -> Unit,
    onSpinWheel: (Boolean) -> WheelSpinOutcome?,
    onSpinAnimationFinished: (WheelSpinOutcome) -> Unit,
    onWatchAdForWheel: () -> Unit = {},
    onClaimAdStep: () -> Unit = {},
    onDismissAd: () -> Unit = {},
    onConsumePendingWheelSpin: () -> Unit = {},
    onOpenThemePicker: () -> Unit,
    onOpenWorkout: (WorkoutProgram) -> Unit,
    onWatchAdForWorkout: (WorkoutProgram) -> Unit,
    onOpenAiCoach: () -> Unit,
    onUpgradePro: () -> Unit = {},
    onBrowseWorkouts: () -> Unit = {},
    onInviteFriends: () -> Unit = {},
    onTestInvite: () -> Unit = {},
    onCopyInviteCode: () -> Unit = onInviteFriends
) {
    val langCode = LocalAppLanguageCode.current
    val locale = LocalAppLocale.current
    val profile = uiState.profile
    val primaryColor = MaterialTheme.colorScheme.primary
    val surfaceColor = MaterialTheme.colorScheme.surface
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    val subtleBorder = remember(primaryColor) { BorderStroke(1.2.dp, primaryColor.copy(alpha = 0.28f)) }
    val flatElevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    val surfaceCardColors = CardDefaults.cardColors(containerColor = surfaceColor)

    var selectedMood by rememberSaveable { mutableStateOf("balanced") }
    var checkedExerciseIds by rememberSaveable { mutableStateOf(emptyList<String>()) }
    var inspectedExercise by remember { mutableStateOf<ExerciseItem?>(null) }
    var showQuestsSheet by rememberSaveable { mutableStateOf(false) }
    var showWheelSheet by rememberSaveable { mutableStateOf(false) }
    var showRankSheet by rememberSaveable { mutableStateOf(false) }

    val todayLabel = remember(langCode) {
        SimpleDateFormat("EEEE • d MMM", locale).format(Date()).replaceFirstChar { it.uppercase() }
    }
    val coachMood = when (selectedMood) {
        "energized" -> CoachAnimationMood.CHEERING
        "gentle" -> CoachAnimationMood.IDLE
        else -> CoachAnimationMood.COACHING
    }

    inspectedExercise?.let { exercise ->
        ExerciseDetailBottomSheet(
            exercise = exercise,
            onDismiss = { inspectedExercise = null },
            gender = profile.gender
        )
    }

    if (showQuestsSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showQuestsSheet = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.background,
            modifier = Modifier.testTag("home_quests_sheet")
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 32.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = strRes(R.string.todays_quests),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { showQuestsSheet = false },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.Rounded.Close, contentDescription = strRes(R.string.common_close))
                    }
                }

                DailyQuestsAndChallengeCard(
                    profile = profile,
                    onTapCoachQuest = onTapChicken,
                    onCompleteChallengeQuest = { onCompleteQuest("challenge") },
                    onCompleteHydrationQuest = { onCompleteQuest("hydration") }
                )

                DailyStreakAndLevelCard(
                    profile = profile,
                    onClaimCheckIn = onClaimDailyCheckIn,
                    selectedMood = selectedMood,
                    onSelectMood = { selectedMood = it }
                )
            }
        }
    }

    if (showRankSheet) {
        RankProgressSheet(
            profile = profile,
            onDismiss = { showRankSheet = false }
        )
    }

    if (showWheelSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showWheelSheet = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.background,
            modifier = Modifier.testTag("home_wheel_sheet")
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 32.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = strRes(R.string.wheel_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { showWheelSheet = false },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.Rounded.Close, contentDescription = strRes(R.string.common_close))
                    }
                }

                DailyXpProWheelCard(
                    profile = profile,
                    latestOutcome = uiState.latestWheelOutcome,
                    pendingAdWheelSpin = uiState.pendingAdWheelSpin,
                    activeAdSession = uiState.adSession,
                    onSpinWheel = onSpinWheel,
                    onWatchAdForWheel = onWatchAdForWheel,
                    onClaimAdStep = onClaimAdStep,
                    onDismissAd = onDismissAd,
                    onConsumePendingWheelSpin = onConsumePendingWheelSpin,
                    onSpinAnimationFinished = onSpinAnimationFinished
                )
            }
        }
    }

    val activeWorkout = remember(personalizedWorkouts, profile.isProActive) {
        personalizedWorkouts.firstOrNull { isWorkoutAccessible(it) } ?: personalizedWorkouts.firstOrNull()
    }

    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // 1. Modern Header + Goal / Body-Fat Summary Strip
        item(key = "home_greeting_header", contentType = "header") {
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        color = primaryColor.copy(alpha = 0.12f),
                        shape = CircleShape,
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        Text(
                            text = todayLabel,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = primaryColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.surface,
                            shape = CircleShape,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                        ) {
                            IconButton(
                                onClick = onOpenThemePicker,
                                modifier = Modifier
                                    .size(48.dp)
                                    .testTag("open_theme_picker_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Palette,
                                    contentDescription = strRes(R.string.theme_studio_title),
                                    tint = primaryColor
                                )
                            }
                        }

                        Surface(
                            color = if (profile.isProActive) primaryColor else surfaceColor,
                            shape = CircleShape,
                            border = subtleBorder,
                            modifier = Modifier
                                .heightIn(min = 48.dp)
                                .clip(CircleShape)
                                .clickable(onClick = onUpgradePro)
                                .testTag("home_header_plan_badge")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Icon(
                                    imageVector = if (profile.isProActive) Icons.Rounded.Star else Icons.Rounded.Lock,
                                    contentDescription = null,
                                    tint = if (profile.isProActive) MaterialTheme.colorScheme.onPrimary else primaryColor,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = strRes(if (profile.isProActive) R.string.plan_pro else R.string.plan_free),
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    maxLines = 1,
                                    softWrap = false,
                                    color = if (profile.isProActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                Text(
                    text = "${strRes(R.string.hello)}, ${profile.displayName}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )

                // Clean Athletic Goal & Body-Fat Metrics Strip
                Surface(
                    color = surfaceColor,
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 12.dp)
                    ) {
                        HomeGoalMetricTile(
                            icon = Icons.Rounded.MonitorWeight,
                            label = strRes(R.string.tab_weight),
                            value = "${profile.currentWeightKg.roundToInt()} → ${profile.targetWeightKg.roundToInt()} kg",
                            modifier = Modifier.weight(1f)
                        )
                        HorizontalDivider(
                            modifier = Modifier
                                .height(32.dp)
                                .width(1.dp),
                            color = MaterialTheme.colorScheme.outline
                        )
                        HomeGoalMetricTile(
                            icon = Icons.Rounded.FitnessCenter,
                            label = strRes(R.string.macro_fat),
                            value = "${formatBodyFatLabel(profile.bodyFatCurrentPct, langCode)} → ${formatBodyFatLabel(profile.bodyFatTargetPct, langCode)}",
                            modifier = Modifier.weight(1f)
                        )
                        HorizontalDivider(
                            modifier = Modifier
                                .height(32.dp)
                                .width(1.dp),
                            color = MaterialTheme.colorScheme.outline
                        )
                        HomeGoalMetricTile(
                            icon = Icons.Rounded.LocalFireDepartment,
                            label = strRes(R.string.target_label),
                            value = "${profile.dailyCalorieTarget} kcal",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // 2. Quick Coach Card
        item(key = "home_coach_hero", contentType = "coach_hero") {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = surfaceCardColors,
                border = BorderStroke(1.2.dp, primaryColor.copy(alpha = 0.34f)),
                elevation = flatElevation,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onOpenAiCoach)
                    .testTag("tappable_chicken_coach_card")
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawWithCache {
                            val brush = Brush.radialGradient(
                                colors = listOf(
                                    primaryColor.copy(alpha = 0.16f),
                                    tertiaryColor.copy(alpha = 0.06f),
                                    Color.Transparent
                                ),
                                center = Offset(size.width * 0.18f, size.height * 0.32f),
                                radius = size.maxDimension * 0.72f
                            )
                            onDrawBehind {
                                drawRect(brush = brush)
                            }
                        }
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        val shownAdvice = if (selectedMood == "gentle" && uiState.tipTapCount == 0) {
                            strRes(R.string.coach_gentle_fallback)
                        } else {
                            uiState.currentCoachTip
                        }

                        // Keep the mascot and heading together, then give the coach message
                        // the full card width so longer translations do not get truncated.
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AnimatedKocTavuk(
                                size = 86.dp,
                                mood = coachMood,
                                triggerKey = "${uiState.tipTapCount}_$selectedMood",
                                showAura = false,
                                contentDescription = strRes(R.string.home_ai_card_title)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Surface(
                                    color = primaryColor,
                                    shape = CircleShape,
                                    modifier = Modifier.testTag("home_ai_card_badge")
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.AutoAwesome,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier.size(11.dp)
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(
                                            text = strRes(R.string.home_ai_card_badge),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }

                                Text(
                                    text = strRes(R.string.home_ai_card_title),
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.42f),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("home_ai_card_advice_bubble")
                        ) {
                            Text(
                                text = shownAdvice,
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 12.sp,
                                lineHeight = 17.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 11.dp, vertical = 9.dp)
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = onOpenAiCoach,
                                shape = CircleShape,
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = 42.dp)
                                    .testTag("home_ai_card_open_cta")
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.AutoAwesome,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = strRes(R.string.home_ai_card_cta),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            OutlinedButton(
                                onClick = onTapChicken,
                                shape = CircleShape,
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = 42.dp)
                                    .testTag("home_ai_card_tip_action")
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Refresh,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = strRes(
                                        if (profile.questCoachDone) R.string.home_ai_card_tip_btn
                                        else R.string.coach_tap_claim_xp
                                    ),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2b. Unified Invite 5 Friends = 1 Month Free PRO Card
        item(key = "home_invite_pro", contentType = "invite_pro_card") {
            InviteFriendsProCard(
                profile = profile,
                onInviteFriends = onInviteFriends,
                onTestInvite = onTestInvite,
                onCopyInviteCode = onCopyInviteCode,
                cardTestTag = "home_invite_pro_card"
            )
        }

        // 3. Streak / Rank Card (opens Quests & PRO Wheel modals)
        item(key = "home_rank_xp", contentType = "rank_xp") {
            RankAndXpHeroCard(
                profile = profile,
                onClaimCheckIn = onClaimDailyCheckIn,
                onOpenQuests = { showQuestsSheet = true },
                onOpenWheel = { showWheelSheet = true },
                onOpenRanks = { showRankSheet = true }
            )
        }

        // 4. Custom Tailored Workout Card (Strictly PRO-gated: hidden & locked when not PRO)
        item(key = "home_active_checklist", contentType = "workout_checklist") {
            if (profile.isProActive && activeWorkout != null) {
                if (isWorkoutAccessible(activeWorkout)) {
                    DailyWorkoutChecklistCard(
                        workout = activeWorkout,
                        checkedIds = checkedExerciseIds.toSet(),
                        onToggleExercise = { exId ->
                            checkedExerciseIds = if (checkedExerciseIds.contains(exId)) {
                                checkedExerciseIds - exId
                            } else {
                                checkedExerciseIds + exId
                            }
                        },
                        onInspectExercise = { ex -> inspectedExercise = ex },
                        onStartWorkout = { onOpenWorkout(activeWorkout) },
                        isProActive = true
                    )
                } else {
                    val adsWatched = adsWatchedForWorkout(activeWorkout.id)
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = surfaceCardColors,
                        border = subtleBorder,
                        elevation = flatElevation,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("dashboard_workout_checklist_card")
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.padding(18.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Rounded.Lock, contentDescription = null, tint = primaryColor)
                                Text(
                                    text = activeWorkout.localizedTitle(),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Button(
                                    onClick = { onWatchAdForWorkout(activeWorkout) },
                                    shape = CircleShape,
                                    modifier = Modifier
                                        .weight(1f)
                                        .heightIn(min = 48.dp)
                                ) {
                                    Icon(Icons.Rounded.PlayCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "${strRes(R.string.watch_ads_prefix)} ($adsWatched/3)",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                OutlinedButton(
                                    onClick = onUpgradePro,
                                    shape = CircleShape,
                                    modifier = Modifier.heightIn(min = 48.dp)
                                ) {
                                    Text(
                                        text = strRes(R.string.unlock_with_pro),
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                LockedProCustomWorkoutCard(
                    onUpgradePro = onUpgradePro,
                    onBrowseWorkouts = onBrowseWorkouts
                )
            }
        }
    }
}

@Composable
private fun HomeGoalMetricTile(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val primary = MaterialTheme.colorScheme.primary
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp),
        modifier = modifier.padding(horizontal = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = primary,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun LockedProCustomWorkoutCard(
    onUpgradePro: () -> Unit,
    onBrowseWorkouts: () -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary
    val surface = MaterialTheme.colorScheme.surface
    val tertiary = MaterialTheme.colorScheme.tertiary

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = surface),
        border = BorderStroke(1.2.dp, primary.copy(alpha = 0.30f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("dashboard_workout_checklist_card")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .drawWithCache {
                    val wash = Brush.linearGradient(
                        colors = listOf(
                            primary.copy(alpha = 0.12f),
                            surface,
                            tertiary.copy(alpha = 0.08f)
                        )
                    )
                    onDrawBehind { drawRect(brush = wash) }
                }
                .padding(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        color = primary.copy(alpha = 0.14f),
                        shape = CircleShape,
                        border = BorderStroke(1.dp, primary.copy(alpha = 0.35f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Lock,
                                contentDescription = null,
                                tint = primary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = strRes(R.string.plan_pro),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = primary
                            )
                        }
                    }

                    Surface(
                        color = MaterialTheme.colorScheme.background,
                        shape = CircleShape,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Rounded.Lock,
                                contentDescription = strRes(R.string.checklist_pro_locked_title),
                                tint = primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = strRes(R.string.checklist_pro_locked_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = strRes(R.string.checklist_pro_locked_sub),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    LockedFeaturePreviewRow(text = strRes(R.string.checklist_pro_locked_feature_1))
                    LockedFeaturePreviewRow(text = strRes(R.string.checklist_pro_locked_feature_2))
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onUpgradePro,
                        shape = CircleShape,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp)
                            .testTag("dashboard_locked_pro_unlock_cta")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Star,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = strRes(R.string.checklist_unlock_pro_cta),
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    OutlinedButton(
                        onClick = onBrowseWorkouts,
                        shape = CircleShape,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp)
                            .testTag("dashboard_browse_free_workouts_cta")
                    ) {
                        Text(
                            text = strRes(R.string.checklist_free_browse_cta),
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LockedFeaturePreviewRow(text: String) {
    Surface(
        color = MaterialTheme.colorScheme.background.copy(alpha = 0.85f),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Lock,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(15.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

