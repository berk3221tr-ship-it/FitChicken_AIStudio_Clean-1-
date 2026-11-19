package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.model.UserProfile
import com.example.ui.i18n.strRes

private data class RankTierUi(
    val index: Int,
    val titleRes: Int,
    val minimumXp: Int,
    val maximumXp: Int?
)

private val rankTiers = listOf(
    RankTierUi(0, R.string.rank_beginner, 0, 499),
    RankTierUi(1, R.string.rank_fit_novice, 500, 1_499),
    RankTierUi(2, R.string.rank_fitness_star, 1_500, 2_999),
    RankTierUi(3, R.string.rank_rising_champion, 3_000, 4_999),
    RankTierUi(4, R.string.rank_champion, 5_000, 7_499),
    RankTierUi(5, R.string.rank_elite_athlete, 7_500, 9_999),
    RankTierUi(6, R.string.rank_legend, 10_000, null)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RankProgressSheet(
    profile: UserProfile,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val currentIndex = profile.chickenRankIndex
    val bestStreak = maxOf(profile.bestStreakDays, profile.streakDays)
    val milestones = listOf(
        1 to R.string.rank_milestone_day_1,
        7 to R.string.rank_milestone_day_7,
        30 to R.string.rank_milestone_day_30,
        90 to R.string.rank_milestone_day_90
    )
    val nextMilestone = milestones.firstOrNull { bestStreak < it.first }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.testTag("rank_progress_sheet")
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Rounded.EmojiEvents,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(34.dp)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = strRes(R.string.rank_sheet_title),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = strRes(R.string.rank_sheet_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onDismiss, modifier = Modifier.size(48.dp)) {
                    Icon(Icons.Rounded.Close, contentDescription = strRes(R.string.common_close))
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.55f)),
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    RankInsigniaBadge(
                        rankIndex = currentIndex,
                        size = 82.dp,
                        contentDescription = strRes(profile.chickenRankTitleRes())
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = strRes(profile.chickenRankTitleRes()),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 2,
                            softWrap = true
                        )
                        Text(
                            text = "${profile.totalXp} XP  •  ${strRes(R.string.level_prefix)}${profile.chickenLevel}",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = strRes(R.string.rank_workouts_completed_fmt, profile.workoutsCompletedCount),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (!profile.isMaxRank) {
                            Text(
                                text = strRes(
                                    R.string.next_rank_xp_needed_fmt,
                                    profile.remainingXpForNextRank
                                ),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            Text(
                                text = strRes(R.string.max_rank_unlocked),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Rank list is vertical and wraps naturally, so it remains usable on compact phones.
            Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
                Text(
                    text = strRes(R.string.rank_sheet_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold
                )
                rankTiers.forEach { tier ->
                    val isCurrent = tier.index == currentIndex
                    val isUnlocked = tier.index < currentIndex
                    val statusText = when {
                        isCurrent -> strRes(R.string.rank_status_current)
                        isUnlocked -> strRes(R.string.rank_status_unlocked)
                        else -> strRes(R.string.rank_status_locked)
                    }
                    val statusColor = if (isCurrent) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isCurrent) MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)
                            else MaterialTheme.colorScheme.surface
                        ),
                        border = BorderStroke(
                            if (isCurrent) 1.8.dp else 1.dp,
                            if (isCurrent) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
                        ),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.fillMaxWidth().testTag("rank_tier_${tier.index}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            RankInsigniaBadge(
                                rankIndex = tier.index,
                                size = 66.dp,
                                isTargetPreview = !isUnlocked && !isCurrent,
                                contentDescription = strRes(tier.titleRes)
                            )
                            Column(
                                verticalArrangement = Arrangement.spacedBy(3.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = strRes(tier.titleRes),
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 2,
                                    softWrap = true
                                )
                                val xpRange = if (tier.maximumXp == null) {
                                    strRes(R.string.rank_xp_from_fmt, tier.minimumXp)
                                } else {
                                    strRes(R.string.rank_xp_range_fmt, tier.minimumXp, tier.maximumXp)
                                }
                                Text(
                                    text = xpRange,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Column(
                                horizontalAlignment = Alignment.End,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = when {
                                        isCurrent -> Icons.Rounded.Star
                                        isUnlocked -> Icons.Rounded.CheckCircle
                                        else -> Icons.Rounded.Lock
                                    },
                                    contentDescription = null,
                                    tint = statusColor,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = statusText,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = statusColor,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.End
                                )
                            }
                        }
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = strRes(R.string.rank_milestones_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold
                )
                milestones.forEach { (days, titleRes) ->
                    val reached = bestStreak >= days
                    Surface(
                        color = if (reached) MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)
                        else MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(
                            1.dp,
                            if (reached) MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)
                            else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Icon(
                                imageVector = if (reached) Icons.Rounded.CheckCircle else Icons.Rounded.Lock,
                                contentDescription = null,
                                tint = if (reached) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = strRes(titleRes),
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = strRes(R.string.rank_milestone_day_fmt, days),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = strRes(if (reached) R.string.rank_milestone_unlocked else R.string.rank_milestone_locked),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (reached) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                if (nextMilestone != null) {
                    val daysLeft = (nextMilestone.first - bestStreak).coerceAtLeast(0)
                    Text(
                        text = strRes(R.string.rank_milestone_next_fmt, nextMilestone.first, daysLeft),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Text(
                        text = strRes(R.string.rank_milestone_peak_reached),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
