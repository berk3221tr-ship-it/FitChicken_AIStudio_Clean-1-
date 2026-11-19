package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.catalog.AzExerciseDatabase
import com.example.data.catalog.CoachWisdomAndStories
import com.example.data.catalog.ExerciseMediaCatalog
import com.example.data.catalog.WorkoutCatalogHomeAndYouth
import com.example.data.catalog.localizedBreathingCue
import com.example.data.catalog.localizedCommonMistake
import com.example.data.catalog.localizedDifficulty
import com.example.data.catalog.localizedFormCue
import com.example.data.catalog.localizedName
import com.example.data.catalog.localizedSecondaryMuscles
import com.example.data.catalog.localizedSetsReps
import com.example.data.catalog.localizedSteps
import com.example.data.catalog.localizedSubtitle
import com.example.data.catalog.localizedTargetMuscle
import com.example.data.catalog.localizedTitle
import com.example.data.local.CustomWorkoutMapper
import com.example.data.model.UserProfile
import com.example.data.model.WorkoutCategory
import com.example.data.model.WorkoutProgram
import com.example.audio.SoundEffectsManager
import com.example.ui.i18n.AppLanguage
import com.example.ui.components.formatBodyFatLabel
import com.example.ui.i18n.LocaleHelper
import com.example.ui.screens.normalizeWorkoutSearchText
import com.example.ui.theme.AppThemeOption
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class LocalizationAndMediaTest {

    @Test
    fun `defaults auto-detect system language and use Light Orange mascot theme`() {
        val defaults = UserProfile()
        assertEquals(AppLanguage.systemDefaultCode(), defaults.appLanguage)
        assertTrue(defaults.isLanguageSelected)
        assertEquals("light_orange", defaults.themeId)
        assertEquals("", defaults.focusAreasCsv)
        assertEquals("kg", defaults.weightUnit)

        assertEquals(2, AppThemeOption.entries.size)
        assertEquals(listOf(AppThemeOption.LIGHT_ORANGE, AppThemeOption.DARK_ORANGE), AppThemeOption.entries)
        assertEquals(AppThemeOption.LIGHT_ORANGE, AppThemeOption.fromId("unknown"))
        assertEquals(AppThemeOption.LIGHT_ORANGE, AppThemeOption.fromId("light_blue"))
        assertEquals(AppThemeOption.DARK_ORANGE, AppThemeOption.fromId("dark_orange"))
    }

    @Test
    fun `100 XP leveling, mascot ranks, and badges evaluate accurately`() {
        val baseContext = ApplicationProvider.getApplicationContext<Context>()
        val trCtx = LocaleHelper.localizedContext(baseContext, "tr")
        val enCtx = LocaleHelper.localizedContext(baseContext, "en")

        val beginner = UserProfile(totalXp = 99)
        assertEquals(1, beginner.chickenLevel)
        assertEquals(99, beginner.xpInCurrentLevel)
        assertEquals("Beginner", enCtx.getString(beginner.chickenRankTitleRes()))
        assertEquals("Başlangıç", trCtx.getString(beginner.chickenRankTitleRes()))

        val level2 = UserProfile(totalXp = 100)
        assertEquals(2, level2.chickenLevel)
        assertEquals(0, level2.xpInCurrentLevel)

        val fitNovice = UserProfile(totalXp = 500)
        assertEquals(6, fitNovice.chickenLevel)
        assertEquals("Fit Novice", enCtx.getString(fitNovice.chickenRankTitleRes()))
        assertEquals("Fit Acemi", trCtx.getString(fitNovice.chickenRankTitleRes()))

        val fitnessStar = UserProfile(totalXp = 1_500)
        assertEquals(16, fitnessStar.chickenLevel)
        assertEquals("Fitness Star", enCtx.getString(fitnessStar.chickenRankTitleRes()))
        assertEquals("Fitness Yıldızı", trCtx.getString(fitnessStar.chickenRankTitleRes()))

        val risingChampion = UserProfile(totalXp = 3_000)
        assertEquals(31, risingChampion.chickenLevel)
        assertEquals("Rising Champion", enCtx.getString(risingChampion.chickenRankTitleRes()))
        assertEquals("Yükselen Şampiyon", trCtx.getString(risingChampion.chickenRankTitleRes()))

        assertEquals("Champion", enCtx.getString(UserProfile(totalXp = 5_000).chickenRankTitleRes()))
        assertEquals("Elite Athlete", enCtx.getString(UserProfile(totalXp = 7_500).chickenRankTitleRes()))
        assertEquals("Legend", enCtx.getString(UserProfile(totalXp = 10_000).chickenRankTitleRes()))
        assertEquals(500, beginner.nextRankLevel)
        assertEquals(401, beginner.remainingXpForNextRank)
        assertEquals(0.198f, beginner.nextRankProgress, 0.001f)
        assertEquals(1f, UserProfile(totalXp = 10_000).nextRankProgress)
        assertEquals("gogus sinav", normalizeWorkoutSearchText("GÖĞÜS — ŞINAV"))
        assertEquals("deadlift", normalizeWorkoutSearchText("Deadlift!"))

        val lockedBadges = com.example.data.model.ChickenBadges.evaluate(UserProfile(), calorieLogsCount = 0)
        assertEquals(27, lockedBadges.size)
        assertTrue(lockedBadges.none { it.unlocked })

        val progressedProfile = UserProfile(
            totalXp = 500,
            workoutsCompletedCount = 1,
            streakDays = 3,
            mealScansCount = 1
        )
        val unlockedIds = com.example.data.model.ChickenBadges.evaluate(progressedProfile, calorieLogsCount = 0)
            .filter { it.unlocked }
            .map { it.id }
            .toSet()
        assertEquals(setOf("first_egg", "flame_chick", "smart_nutrition", "gym_bro_chicken"), unlockedIds)
    }

    @Test
    fun `core 40 workouts translate across all 10 languages and all 200 workouts resolve cleanly`() {
        val baseContext = ApplicationProvider.getApplicationContext<Context>()
        val coreWorkouts = WorkoutCatalogHomeAndYouth.CORE_40_WORKOUTS
        val allWorkouts = WorkoutCatalogHomeAndYouth.ALL_WORKOUTS
        assertEquals(40, coreWorkouts.size)
        assertEquals(200, allWorkouts.size)
        val coreExerciseCount = coreWorkouts.sumOf { it.exercises.size }
        assertEquals(130, coreExerciseCount)

        AppLanguage.entries.forEach { lang ->
            val ctx = LocaleHelper.localizedContext(baseContext, lang.code)

            val scannerLabel = ctx.getString(R.string.nav_calorie_scanner)
            assertTrue("Blank nav_calorie_scanner in ${lang.code}", scannerLabel.isNotBlank())

            val tips = CoachWisdomAndStories.tips(ctx)
            assertEquals("Expected 40 tips in ${lang.code}", 40, tips.size)
            assertTrue(tips.all { it.isNotBlank() })

            val stories = CoachWisdomAndStories.stories(ctx)
            assertEquals("Expected 4 stories in ${lang.code}", 4, stories.size)
            assertTrue(stories.all { it.badge.isNotBlank() && it.resultSummary.isNotBlank() && it.quote.isNotBlank() })

            // Core 40 XML translations check across all 10 languages
            coreWorkouts.forEach { workout ->
                val title = workout.localizedTitle(ctx)
                val subtitle = workout.localizedSubtitle(ctx)
                val difficulty = workout.localizedDifficulty(ctx)
                assertTrue("Blank title for ${workout.id} in ${lang.code}", title.isNotBlank())
                assertTrue("Blank subtitle for ${workout.id} in ${lang.code}", subtitle.isNotBlank())
                assertTrue("Blank difficulty for ${workout.id} in ${lang.code}", difficulty.isNotBlank())
                if (lang != AppLanguage.TR) {
                    assertTrue(
                        "Workout ${workout.id} stayed untranslated Turkish in ${lang.code}: $title",
                        title != workout.title
                    )
                }

                workout.exercises.forEach { ex ->
                    assertTrue("Blank name for ${ex.id} in ${lang.code}", ex.localizedName(ctx).isNotBlank())
                    assertTrue("Blank muscle for ${ex.id} in ${lang.code}", ex.localizedTargetMuscle(ctx).isNotBlank())
                    assertTrue("Blank setsReps for ${ex.id} in ${lang.code}", ex.localizedSetsReps(ctx).isNotBlank())
                    val steps = ex.localizedSteps(ctx)
                    assertTrue("Empty steps for ${ex.id} in ${lang.code}", steps.isNotEmpty() && steps.all { it.isNotBlank() })
                    assertTrue("Blank formCue for ${ex.id} in ${lang.code}", ex.localizedFormCue(ctx).isNotBlank())
                    assertTrue("Blank breathingCue for ${ex.id} in ${lang.code}", ex.localizedBreathingCue(ctx).isNotBlank())
                    assertTrue("Blank commonMistake for ${ex.id} in ${lang.code}", ex.localizedCommonMistake(ctx).isNotBlank())
                }
            }
        }

        // All 200 workouts & A-Z exercises localize EN + TR completely
        val trCtx = LocaleHelper.localizedContext(baseContext, "tr")
        val enCtx = LocaleHelper.localizedContext(baseContext, "en")
        allWorkouts.forEach { workout ->
            assertTrue("${workout.id} blank EN title", workout.localizedTitle(enCtx).isNotBlank())
            assertTrue("${workout.id} blank TR title", workout.localizedTitle(trCtx).isNotBlank())
            assertTrue("${workout.id} blank EN difficulty", workout.localizedDifficulty(enCtx).isNotBlank())
            assertTrue("${workout.id} blank TR difficulty", workout.localizedDifficulty(trCtx).isNotBlank())
        }
        AzExerciseDatabase.ALL_EXERCISES.forEach { entry ->
            val ex = entry.item
            assertTrue("${ex.id} blank EN name", ex.localizedName(enCtx).isNotBlank())
            assertTrue("${ex.id} blank TR name", ex.localizedName(trCtx).isNotBlank())
            assertTrue("${ex.id} blank EN muscle", ex.localizedTargetMuscle(enCtx).isNotBlank())
            assertTrue("${ex.id} blank TR muscle", ex.localizedTargetMuscle(trCtx).isNotBlank())
            assertTrue("${ex.id} blank EN secondary", ex.localizedSecondaryMuscles(enCtx).isNotBlank())
            assertTrue("${ex.id} blank TR secondary", ex.localizedSecondaryMuscles(trCtx).isNotBlank())
        }
    }

    @Test
    fun `every exercise ID across 200 workouts and A-Z DB has a free-exercise-db media mapping and muscle zone`() {
        val workoutExIds = WorkoutCatalogHomeAndYouth.ALL_WORKOUTS
            .flatMap { it.exercises }
            .map { it.id }
            .toSet()
        val azIds = AzExerciseDatabase.ALL_EXERCISES.map { it.item.id }.toSet()
        val allIds = workoutExIds + azIds

        val mappedIds = ExerciseMediaCatalog.allMappedIds()
        assertTrue("All exercise IDs must be mapped in ExerciseMediaCatalog", mappedIds.containsAll(allIds))

        allIds.forEach { id ->
            val spec = ExerciseMediaCatalog.mediaFor(id)
            assertTrue("$id startFrameUrl invalid", spec.startFrameUrl.endsWith("/0.jpg"))
            assertTrue("$id peakFrameUrl invalid", spec.peakFrameUrl.endsWith("/1.jpg"))
            assertFalse("$id media URL contains obsolete /images path", spec.startFrameUrl.contains("/images"))
            assertNotEquals("$id start/peak images must differ", spec.startFrameUrl, spec.peakFrameUrl)
        }
        assertEquals("Hammer_Curls", ExerciseMediaCatalog.exactMediaFor("g5_4")?.dbFolderId)
        assertEquals("EZ-Bar_Curl", ExerciseMediaCatalog.exactMediaFor("g2_4")?.dbFolderId)
        assertEquals("Incline_Dumbbell_Curl", ExerciseMediaCatalog.exactMediaFor("g6_2")?.dbFolderId)
        assertTrue("Movement without a verified exact demo must show a placeholder", ExerciseMediaCatalog.exactMediaFor("h9_2") == null)
    }

    @Test
    fun `custom workout mapper round-trips exercises and customized sets and reps cleanly`() {
        val sampleExercises = listOf(
            AzExerciseDatabase.findById("az_barbell_bench_press")!!.item.copy(setsReps = "4 x 8"),
            AzExerciseDatabase.findById("az_goblet_squat")!!.item.copy(setsReps = "3 x 12"),
            AzExerciseDatabase.findById("az_plank")!!.item.copy(setsReps = "3 x 45s")
        )
        val original = WorkoutProgram(
            id = "custom_test123",
            title = "Friday Mix",
            subtitle = "3 custom exercises",
            category = WorkoutCategory.HOME,
            focusArea = "full_body",
            difficulty = "Intermediate",
            durationMinutes = 26,
            estimatedCalories = 208,
            exercises = sampleExercises,
            isCustom = true,
            heroImageUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Plank/0.jpg",
            imagePrompt = "Custom prompt",
            titleTr = "Friday Mix"
        )

        val entity = CustomWorkoutMapper.toEntity(original, createdAtMillis = 123456L)
        val decoded = CustomWorkoutMapper.toProgram(entity)

        assertTrue(decoded.isCustom)
        assertEquals("custom_test123", decoded.id)
        assertEquals("Friday Mix", decoded.title)
        assertEquals(WorkoutCategory.HOME, decoded.category)
        assertEquals(3, decoded.exercises.size)
        assertEquals("4 x 8", decoded.exercises[0].setsReps)
        assertEquals("az_goblet_squat", decoded.exercises[1].id)
        assertEquals("3 x 45s", decoded.exercises[2].setsReps)
    }

    @Test
    fun `exact exercise media mappings exclude unverified placeholders and map distinct curl folders`() {
        val allExercises = (WorkoutCatalogHomeAndYouth.ALL_WORKOUTS.flatMap { it.exercises } +
            AzExerciseDatabase.ALL_EXERCISES.map { it.item }).distinctBy { it.id }

        assertTrue(allExercises.isNotEmpty())
        val unverifiedIds = setOf("h9_2", "h9_3", "h10_1", "h10_3", "h11_1", "h11_2", "h11_3", "h12_2")
        allExercises.forEach { ex ->
            val exact = ExerciseMediaCatalog.exactMediaFor(ex.id)
            if (ex.id in unverifiedIds) {
                assertEquals("${ex.id} should return null exactMediaFor", null, exact)
            } else {
                assertTrue("${ex.id} should resolve exactMediaFor", exact != null)
                assertNotEquals("${ex.id} start/peak URLs must differ", exact?.startFrameUrl, exact?.peakFrameUrl)
            }
        }
    }

    @Test
    fun `invite card, workouts filters, planner hint, quests and sound strings resolve across all 10 locales`() {
        val baseContext = ApplicationProvider.getApplicationContext<Context>()
        val simpleKeys = listOf(
            R.string.pro_card_unlocked_title,
            R.string.pro_card_unlocked_sub,
            R.string.invite_badge_title,
            R.string.invite_card_unlock_title,
            R.string.invite_card_unlock_desc,
            R.string.invite_friends_cta,
            R.string.invite_test_plus_one,
            R.string.planner_referral_unlocked_banner,
            R.string.planner_open_workout_hint,
            R.string.workouts_empty_title,
            R.string.workouts_reset_search,
            R.string.filter_for_you,
            R.string.focus_badge_upper,
            R.string.focus_badge_lower,
            R.string.focus_badge_core,
            R.string.focus_badge_full,
            R.string.onb_sound_title,
            R.string.onb_sound_sub,
            R.string.onb_sound_yes,
            R.string.onb_sound_yes_sub,
            R.string.onb_sound_no,
            R.string.onb_sound_no_sub,
            R.string.settings_sound_title,
            R.string.settings_sound_sub,
            R.string.settings_sound_on,
            R.string.settings_sound_off
        )

        val enCtx = LocaleHelper.localizedContext(baseContext, "en")
        val enUnlockTitle = enCtx.getString(R.string.invite_card_unlock_title)

        AppLanguage.entries.forEach { lang ->
            val ctx = LocaleHelper.localizedContext(baseContext, lang.code)
            simpleKeys.forEach { resId ->
                val text = ctx.getString(resId)
                assertTrue("Blank string for resId=$resId in ${lang.code}", text.isNotBlank())
            }
            assertTrue(ctx.getString(R.string.invite_progress_invited_fmt, 2).contains("2"))
            assertTrue(ctx.getString(R.string.invite_added_status_fmt, 2, 3).contains("2"))
            assertTrue(ctx.getString(R.string.workouts_empty_query_fmt, "squat").contains("squat"))
            assertTrue(ctx.getString(R.string.quests_btn_fmt, 1).contains("1"))
            assertTrue(ctx.getString(R.string.wheel_btn_fmt, 2).contains("2"))

            if (lang.code != "en") {
                assertFalse(
                    "invite_card_unlock_title untranslated in ${lang.code}",
                    ctx.getString(R.string.invite_card_unlock_title) == enUnlockTitle
                )
            }
        }
    }

    @Test
    fun `workout catalog titles use natural non-gendered fitness terminology`() {
        val baseContext = ApplicationProvider.getApplicationContext<Context>()
        val trCtx = LocaleHelper.localizedContext(baseContext, "tr")
        val enCtx = LocaleHelper.localizedContext(baseContext, "en")
        val bannedFragments = listOf("Heykeltıraşı", "Komşusuz", "(Gym)", "Kadınlar İçin", "Erkekler İçin")

        WorkoutCatalogHomeAndYouth.ALL_WORKOUTS.forEach { workout ->
            val trTitle = workout.localizedTitle(trCtx)
            val enTitle = workout.localizedTitle(enCtx)
            bannedFragments.forEach { banned ->
                assertFalse("Banned '$banned' found in TR title: $trTitle", trTitle.contains(banned))
            }
            assertFalse("Blank EN title for ${workout.id}", enTitle.isBlank())
        }
        assertEquals("Spor Salonu", WorkoutCategory.GYM.label("tr"))
        assertEquals("Spor Salonu", trCtx.getString(R.string.cat_gym))
    }

    @Test
    fun `sound effects manager toggles cleanly, stays safe on Robolectric, and synthesizes PCM audio`() {
        assertTrue(UserProfile().soundEnabled)

        SoundEffectsManager.setEnabled(false)
        assertFalse(SoundEffectsManager.isEnabled())
        SoundEffectsManager.playTap()
        SoundEffectsManager.playSetComplete()

        SoundEffectsManager.setEnabled(true)
        assertTrue(SoundEffectsManager.isEnabled())
        SoundEffectsManager.playWorkoutStart()
        SoundEffectsManager.playSetComplete()
        SoundEffectsManager.playWorkoutComplete()
        SoundEffectsManager.playXpEarned()
        SoundEffectsManager.playProUnlocked()

        val pcm = SoundEffectsManager.synthesizePcm16(
            freqsHz = floatArrayOf(523.25f, 659.25f),
            toneMs = 55,
            peakGain = 0.2f
        )
        assertTrue("Expected synthesized audio buffer > 0", pcm.isNotEmpty())
        assertTrue("Expected non-zero waveform peaks", pcm.any { it != 0.toShort() })
    }
    @Test
    fun `body fat percentage formatting follows each supported locale`() {
        AppLanguage.entries.forEach { language ->
            val label = formatBodyFatLabel(22, language.code)
            assertTrue("Missing number in ${language.code}: $label", label.contains("22"))
            assertTrue("Missing percent sign in ${language.code}: $label", label.contains("%"))
        }
    }

}
