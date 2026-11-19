package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.model.UserProfile
import com.example.ui.i18n.strRes
import kotlin.math.roundToInt

/**
 * Shared "Invite 5 Friends = 1 Month Free PRO!" card rendered identically across Today,
 * Planner, and Profile tabs, backed by unified ViewModel state.
 */
@Composable
fun InviteFriendsProCard(
    profile: UserProfile,
    onInviteFriends: () -> Unit,
    onTestInvite: () -> Unit,
    onCopyInviteCode: () -> Unit = onInviteFriends,
    onRefreshReferrals: (() -> Unit)? = null,
    onRedeemFriendCode: ((String) -> Unit)? = null,
    cardTestTag: String = "invite_friends_pro_card",
    modifier: Modifier = Modifier
) {
    val isPro = profile.isProActive
    val displayCount = if (isPro && profile.proSource == "referral_reward") {
        5
    } else {
        profile.referralCount.coerceIn(0, 5)
    }
    val progress = displayCount / 5f
    val primary = MaterialTheme.colorScheme.primary
    var friendCodeInput by remember { mutableStateOf("") }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(
            width = 1.3.dp,
            color = primary.copy(alpha = if (isPro) 0.45f else 0.42f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isPro) 2.dp else 4.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag(cardTestTag)
    ) {
        if (isPro) {
            // Clean, compact PRO-active card across Today, Planner, and Profile (no 0/5 counter or invite CTA)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Surface(
                    color = primary.copy(alpha = 0.14f),
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        tint = primary,
                        modifier = Modifier
                            .padding(9.dp)
                            .size(20.dp)
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = strRes(R.string.pro_card_unlocked_title),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.testTag("${cardTestTag}_unlocked_badge")
                    )
                    Text(
                        text = strRes(R.string.pro_card_unlocked_sub),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Surface(
                    color = primary.copy(alpha = 0.10f),
                    shape = CircleShape,
                    border = BorderStroke(1.dp, primary.copy(alpha = 0.28f))
                ) {
                    IconButton(
                        onClick = onCopyInviteCode,
                        modifier = Modifier
                            .size(44.dp)
                            .testTag("${cardTestTag}_copy_code")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ContentCopy,
                            contentDescription = strRes(R.string.common_copy),
                            tint = primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Locked / Progress State
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            color = primary.copy(alpha = 0.12f),
                            shape = CircleShape
                        ) {
                            Text(
                                text = strRes(R.string.invite_badge_title),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = primary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                        Text(
                            text = strRes(R.string.invite_card_unlock_title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = strRes(R.string.invite_card_unlock_desc),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Surface(
                        color = primary.copy(alpha = 0.14f),
                        shape = CircleShape
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AutoAwesome,
                            contentDescription = null,
                            tint = primary,
                            modifier = Modifier
                                .padding(10.dp)
                                .size(20.dp)
                        )
                    }
                }

                // Personal Invite Code Strip (shared across all 3 tabs)
                Surface(
                    color = MaterialTheme.colorScheme.background.copy(alpha = 0.85f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp, end = 4.dp, top = 4.dp, bottom = 4.dp)
                    ) {
                        Text(
                            text = strRes(R.string.your_invite_code_fmt, profile.inviteCode),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = onCopyInviteCode,
                            modifier = Modifier
                                .size(40.dp)
                                .testTag("${cardTestTag}_copy_code")
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ContentCopy,
                                contentDescription = strRes(R.string.common_copy),
                                tint = primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                // Progress Bar Section
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = strRes(R.string.invite_progress_invited_fmt, displayCount),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = primary,
                            maxLines = 1,
                            softWrap = false
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${(progress * 100).roundToInt()}%",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                softWrap = false
                            )
                            if (onRefreshReferrals != null) {
                                Spacer(modifier = Modifier.width(4.dp))
                                IconButton(
                                    onClick = onRefreshReferrals,
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Refresh,
                                        contentDescription = strRes(R.string.common_refresh),
                                        tint = primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                    LinearProgressIndicator(
                        progress = { progress },
                        color = primary,
                        trackColor = primary.copy(alpha = 0.16f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(9.dp)
                            .clip(CircleShape)
                    )
                }

                // Action Buttons (Invite Friends + +1 Test Invite)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onInviteFriends,
                        shape = CircleShape,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
                        modifier = Modifier
                            .weight(1.1f)
                            .heightIn(min = 48.dp)
                            .testTag("invite_friends_button")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Share,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = strRes(R.string.invite_friends_cta),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    OutlinedButton(
                        onClick = onTestInvite,
                        shape = CircleShape,
                        border = BorderStroke(1.2.dp, primary),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 48.dp)
                            .testTag("test_invite_button")
                    ) {
                        Text(
                            text = strRes(R.string.invite_test_plus_one),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = primary,
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Optional Friend Invite Redemption (on Profile tab)
                if (onRedeemFriendCode != null) {
                    if (profile.referredByCode.isBlank()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CleanTextField(
                                value = friendCodeInput,
                                onValueChange = { friendCodeInput = it.uppercase().take(24) },
                                placeholder = "FIT-AB12C",
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                inputModifier = Modifier.testTag("friend_invite_code_input")
                            )
                            Button(
                                onClick = { onRedeemFriendCode(friendCodeInput) },
                                enabled = friendCodeInput.trim().length >= 4,
                                shape = CircleShape,
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                                modifier = Modifier.heightIn(min = 48.dp)
                            ) {
                                Text(
                                    text = strRes(R.string.apply_friend_code),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                    } else {
                        Text(
                            text = strRes(R.string.applied_code_fmt, profile.referredByCode),
                            style = MaterialTheme.typography.labelLarge,
                            color = primary
                        )
                    }
                }
            }
        }
    }
}
