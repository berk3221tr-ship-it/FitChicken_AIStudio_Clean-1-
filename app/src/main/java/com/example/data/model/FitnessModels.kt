package com.example.data.model

import androidx.annotation.StringRes
import com.example.R
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

enum class WorkoutCategory(
    val labelTr: String,
    val labelEn: String,
    @get:StringRes val labelRes: Int
) {
    GYM("Spor Salonu", "Gym", R.string.cat_gym),
    HOME("Evde Antrenman", "Home Workout", R.string.cat_home),
    YOUTH("0-18 Yaş Büyüme Çağı", "Ages 0-18 Growth", R.string.cat_youth);

    fun label(lang: String): String = when (lang.lowercase(java.util.Locale.ROOT)) {
        "tr" -> labelTr
        "de" -> when (this) { GYM -> "Fitnessstudio"; HOME -> "Training zu Hause"; YOUTH -> "Wachstum im Alter von 0–18" }
        "fr" -> when (this) { GYM -> "Salle de sport"; HOME -> "Entraînement à domicile"; YOUTH -> "Croissance de 0 à 18 ans" }
        "es" -> when (this) { GYM -> "Gimnasio"; HOME -> "Entrenamiento en casa"; YOUTH -> "Crecimiento de 0 a 18 años" }
        "it" -> when (this) { GYM -> "Palestra"; HOME -> "Allenamento a casa"; YOUTH -> "Crescita da 0 a 18 anni" }
        "ru" -> when (this) { GYM -> "Тренажёрный зал"; HOME -> "Тренировка дома"; YOUTH -> "Рост в возрасте 0–18 лет" }
        "nl" -> when (this) { GYM -> "Sportschool"; HOME -> "Thuis trainen"; YOUTH -> "Groei van 0–18 jaar" }
        "pl" -> when (this) { GYM -> "Siłownia"; HOME -> "Trening w domu"; YOUTH -> "Rozwój w wieku 0–18 lat" }
        "pt" -> when (this) { GYM -> "Ginásio"; HOME -> "Treino em casa"; YOUTH -> "Crescimento dos 0 aos 18 anos" }
        else -> labelEn
    }
}

enum class ExerciseDemoType {
    SQUAT,
    PUSHUP,
    PLANK,
    PULL,
    PRESS,
    LUNGE,
    JUMP,
    STRETCH,
    CURL,
    CARDIO
}

enum class TargetGroup {
    BOTH,
    MALE_ONLY,
    FEMALE_ONLY
}

data class ExerciseItem(
    val id: String,
    val name: String,
    val targetMuscle: String,
    val setsReps: String,
    val restSeconds: Int,
    val demoType: ExerciseDemoType,
    val steps: List<String>,
    val formCue: String,
    val breathingCue: String,
    val commonMistake: String,
    val targetGroup: TargetGroup = TargetGroup.BOTH,
    val secondaryMuscles: String = "",
    val nameTr: String = "",
    val targetMuscleTr: String = "",
    val secondaryMusclesTr: String = "",
    val stepsTr: List<String> = emptyList(),
    val formCueTr: String = "",
    val breathingCueTr: String = "",
    val commonMistakeTr: String = ""
)

data class WorkoutProgram(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: WorkoutCategory,
    val focusArea: String,
    val difficulty: String,
    val durationMinutes: Int,
    val estimatedCalories: Int,
    val isProSpecial: Boolean = false,
    val isRecommendedForYouth: Boolean = false,
    val exercises: List<ExerciseItem>,
    val targetGroup: TargetGroup = TargetGroup.BOTH,
    val imagePrompt: String = "",
    val heroImageUrl: String = "",
    val isCustom: Boolean = false,
    val titleTr: String = "",
    val subtitleTr: String = ""
) {
    fun matchesGender(gender: String): Boolean = when (gender.lowercase()) {
        "female" -> targetGroup == TargetGroup.BOTH || targetGroup == TargetGroup.FEMALE_ONLY
        "male" -> targetGroup == TargetGroup.BOTH || targetGroup == TargetGroup.MALE_ONLY
        else -> true
    }
}

data class SuccessStory(
    val name: String,
    val age: Int,
    val badge: String,
    val resultSummary: String,
    val quote: String
)

@IgnoreExtraProperties
data class UserProfileDto(
    @DocumentId val id: String = "",
    val userId: String = "",
    val displayName: String = "Sporcu",
    val age: Int = 22,
    val heightCm: Int = 175,
    val startWeightKg: Double = 75.0,
    val currentWeightKg: Double = 75.0,
    val targetWeightKg: Double = 72.0,
    val goal: String = "fitness",
    val environment: String = "hybrid",
    val activityLevel: String = "moderate",
    val experienceLevel: String = "occasional",
    val weeklyDays: Int = 4,
    val sessionMinutes: Int = 40,
    val focusArea: String = "full_body",
    val planTier: String = "free",
    val proSource: String = "none",
    val proExpiresAtMillis: Long = 0L,
    val inviteCode: String = "FIT-1001",
    val referralCount: Int = 0,
    val referredByCode: String = "",
    val aiQuestionsUsedToday: Int = 0,
    val aiQuotaDate: String = "",
    val onboardingCompleted: Boolean = false,
    val streakDays: Int = 0,
    val lastCheckInDate: String = "",
    val totalXp: Int = 0,
    val dailyQuestsDate: String = "",
    val questCoachDone: Boolean = false,
    val questChallengeDone: Boolean = false,
    val questHydrationDone: Boolean = false,
    val workoutsCompletedCount: Int = 0,
    val lastWheelSpinDate: String = "",
    val bestStreakDays: Int = 0,
    val dailySpinCount: Int = 0,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class WeightLogDto(
    @DocumentId val id: String = "",
    val weightKg: Double = 0.0,
    val note: String = "",
    val loggedAtMillis: Long = 0L,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class CalendarEntryDto(
    @DocumentId val id: String = "",
    val dateKey: String = "",
    val workoutId: String = "",
    val workoutTitle: String = "",
    val completed: Boolean = false,
    val durationMinutes: Int = 30,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class ReferralCodeDto(
    @DocumentId val id: String = "",
    val ownerUid: String = "",
    val code: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class ReferralRedemptionDto(
    @DocumentId val id: String = "",
    val code: String = "",
    val codeOwnerUid: String = "",
    val redeemerUid: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null
)

data class UserProfile(
    val displayName: String = "Athlete",
    val age: Int = 22,
    val heightCm: Int = 175,
    val startWeightKg: Double = 75.0,
    val currentWeightKg: Double = 75.0,
    val targetWeightKg: Double = 72.0,
    val goal: String = "fitness",
    val environment: String = "hybrid",
    val activityLevel: String = "moderate",
    val experienceLevel: String = "occasional",
    val weeklyDays: Int = 4,
    val sessionMinutes: Int = 40,
    val focusArea: String = "full_body",
    val sleepQuality: String = "7-8 saat düzenli",
    val nutritionHabit: String = "Dengeli ama geliştirilebilir",
    val planTier: String = "free",
    val proSource: String = "none",
    val proExpiresAtMillis: Long = 0L,
    val inviteCode: String = "FIT-2026",
    val referralCount: Int = 0,
    val referredByCode: String = "",
    val aiQuestionsUsedToday: Int = 0,
    val aiQuotaDate: String = "",
    val onboardingCompleted: Boolean = false,
    val themeId: String = "light_orange",
    val streakDays: Int = 0,
    val lastCheckInDate: String = "",
    val totalXp: Int = 0,
    val dailyQuestsDate: String = "",
    val questCoachDone: Boolean = false,
    val questChallengeDone: Boolean = false,
    val questHydrationDone: Boolean = false,
    val workoutsCompletedCount: Int = 0,
    val gender: String = "male",
    val appLanguage: String = com.example.ui.i18n.AppLanguage.systemDefaultCode(),
    val isLanguageSelected: Boolean = true,
    val lastWheelSpinDate: String = "",
    val bestStreakDays: Int = 0,
    val focusAreasCsv: String = "",
    val bodyFatCurrentPct: Int = 22,
    val bodyFatTargetPct: Int = 15,
    val dailyCalorieTarget: Int = 2100,
    val stressLevel: String = "moderate",
    val badHabitsCsv: String = "",
    val squatCapacity: String = "10_20",
    val wantBetterSleep: Boolean = true,
    val wantLessStress: Boolean = true,
    val motivationLevel: String = "high",
    val planPace: String = "balanced",
    val trainingDaysCsv: String = "mon,wed,fri,sat",
    val notificationsEnabled: Boolean = false,
    val targetDateIso: String = "",
    val weightUnit: String = "kg",
    val mealScansCount: Int = 0,
    val dailySpinCount: Int = 0,
    val soundEnabled: Boolean = true
) {
    val isYouthGrowthMode: Boolean
        get() = age in 0..18

    val isProActive: Boolean
        get() = planTier == "pro" && (proExpiresAtMillis == 0L || proExpiresAtMillis > System.currentTimeMillis())

    val bmi: Double
        get() {
            val hMeters = heightCm / 100.0
            return if (hMeters > 0) currentWeightKg / (hMeters * hMeters) else 0.0
        }

    // User level advances every 100 XP; rank tiers use the seven visible XP brackets.
    val chickenLevel: Int
        get() = (totalXp / 100) + 1

    val xpInCurrentLevel: Int
        get() = totalXp % 100

    val chickenRankIndex: Int
        get() = when {
            totalXp < 500 -> 0
            totalXp < 1_500 -> 1
            totalXp < 3_000 -> 2
            totalXp < 5_000 -> 3
            totalXp < 7_500 -> 4
            totalXp < 10_000 -> 5
            else -> 6
        }

    @StringRes
    fun chickenRankTitleRes(): Int = when (chickenRankIndex) {
        0 -> R.string.rank_beginner
        1 -> R.string.rank_fit_novice
        2 -> R.string.rank_fitness_star
        3 -> R.string.rank_rising_champion
        4 -> R.string.rank_champion
        5 -> R.string.rank_elite_athlete
        else -> R.string.rank_legend
    }

    fun chickenRankTitle(lang: String = appLanguage): String {
        val translations = mapOf(
            "en" to listOf("Beginner", "Fit Novice", "Fitness Star", "Rising Champion", "Champion", "Elite Athlete", "Legend"),
            "tr" to listOf("Başlangıç", "Fit Acemi", "Fitness Yıldızı", "Yükselen Şampiyon", "Şampiyon", "Elit Atlet", "Efsane"),
            "de" to listOf("Anfänger", "Fit-Neuling", "Fitness-Star", "Aufstrebender Champion", "Champion", "Elite-Athlet", "Legende"),
            "fr" to listOf("Débutant", "Novice Fit", "Étoile du fitness", "Champion en devenir", "Champion", "Athlète d'élite", "Légende"),
            "es" to listOf("Principiante", "Novato Fit", "Estrella del fitness", "Campeón en ascenso", "Campeón", "Atleta de élite", "Leyenda"),
            "it" to listOf("Principiante", "Novizio Fit", "Stella del fitness", "Campione emergente", "Campione", "Atleta d'élite", "Leggenda"),
            "ru" to listOf("Новичок", "Фит-новичок", "Звезда фитнеса", "Восходящий чемпион", "Чемпион", "Элитный атлет", "Легенда"),
            "nl" to listOf("Beginner", "Fit-novice", "Fitnessster", "Opkomend kampioen", "Kampioen", "Eliteatleet", "Legende"),
            "pl" to listOf("Początkujący", "Fit Nowicjusz", "Gwiazda fitnessu", "Wschodzący mistrz", "Mistrz", "Elitarny sportowiec", "Legenda"),
            "pt" to listOf("Iniciante", "Novato Fit", "Estrela Fitness", "Campeão em ascensão", "Campeão", "Atleta de elite", "Lenda")
        )
        return translations[lang.lowercase(java.util.Locale.ROOT)]?.get(chickenRankIndex)
            ?: translations.getValue("en")[chickenRankIndex]
    }

    val chickenRankTitle: String
        get() = chickenRankTitle(appLanguage)

    val isMaxRank: Boolean
        get() = chickenRankIndex >= 6

    val nextRankIndex: Int
        get() = (chickenRankIndex + 1).coerceAtMost(6)

    val nextRankLevel: Int
        get() = when (nextRankIndex) {
            1 -> 500
            2 -> 1_500
            3 -> 3_000
            4 -> 5_000
            5 -> 7_500
            else -> 10_000
        }

    @StringRes
    fun nextRankTitleRes(): Int = when (nextRankIndex) {
        1 -> R.string.rank_fit_novice
        2 -> R.string.rank_fitness_star
        3 -> R.string.rank_rising_champion
        4 -> R.string.rank_champion
        5 -> R.string.rank_elite_athlete
        else -> R.string.rank_legend
    }

    val xpForNextRank: Int
        get() = nextRankLevel

    val remainingXpForNextRank: Int
        get() = if (isMaxRank) 0 else (xpForNextRank - totalXp).coerceAtLeast(0)

    val nextRankProgress: Float
        get() {
            if (isMaxRank) return 1f
            val currentRankFloor = when (chickenRankIndex) {
                0 -> 0
                1 -> 500
                2 -> 1_500
                3 -> 3_000
                4 -> 5_000
                5 -> 7_500
                else -> 10_000
            }
            val width = (xpForNextRank - currentRankFloor).coerceAtLeast(1)
            return ((totalXp - currentRankFloor).toFloat() / width).coerceIn(0f, 1f)
        }

}

data class ChickenBadge(
    val id: String,
    val title: String,
    val description: String,
    val unlocked: Boolean
)

object ChickenBadges {
    @StringRes
    fun titleResFor(id: String): Int = when (id) {
        "first_egg" -> R.string.badge_first_egg_title
        "flame_chick" -> R.string.badge_flame_chick_title
        "smart_nutrition" -> R.string.badge_smart_nutrition_title
        "gym_bro_chicken" -> R.string.badge_gym_bro_title
        "iron_rooster" -> R.string.badge_iron_rooster_title
        "alpha_beast" -> R.string.badge_alpha_beast_title
        "streak_7" -> R.string.badge_streak_7_title
        "streak_14" -> R.string.badge_streak_14_title
        "streak_30" -> R.string.badge_streak_30_title
        "workouts_25" -> R.string.badge_workouts_25_title
        "workouts_50" -> R.string.badge_workouts_50_title
        "workouts_100" -> R.string.badge_workouts_100_title
        "meals_5" -> R.string.badge_meals_5_title
        "meals_20" -> R.string.badge_meals_20_title
        "wheel_spin" -> R.string.badge_wheel_spin_title
        "pro_member" -> R.string.badge_pro_member_title
        "early_bird" -> R.string.badge_early_bird_title
        "night_owl" -> R.string.badge_night_owl_title
        "squat_master" -> R.string.badge_squat_master_title
        "hydration_champ" -> R.string.badge_hydration_champ_title
        "breath_master" -> R.string.badge_breath_master_title
        "level_10" -> R.string.badge_level_10_title
        "level_25" -> R.string.badge_level_25_title
        "level_50" -> R.string.badge_level_50_title
        "social_connector" -> R.string.badge_social_connector_title
        "consistency_king" -> R.string.badge_consistency_king_title
        "farm_leader" -> R.string.badge_farm_leader_title
        else -> R.string.badge_first_egg_title
    }

    @StringRes
    fun descriptionResFor(id: String): Int = when (id) {
        "first_egg" -> R.string.badge_first_egg_desc
        "flame_chick" -> R.string.badge_flame_chick_desc
        "smart_nutrition" -> R.string.badge_smart_nutrition_desc
        "gym_bro_chicken" -> R.string.badge_gym_bro_desc
        "iron_rooster" -> R.string.badge_iron_rooster_desc
        "alpha_beast" -> R.string.badge_alpha_beast_desc
        "streak_7" -> R.string.badge_streak_7_desc
        "streak_14" -> R.string.badge_streak_14_desc
        "streak_30" -> R.string.badge_streak_30_desc
        "workouts_25" -> R.string.badge_workouts_25_desc
        "workouts_50" -> R.string.badge_workouts_50_desc
        "workouts_100" -> R.string.badge_workouts_100_desc
        "meals_5" -> R.string.badge_meals_5_desc
        "meals_20" -> R.string.badge_meals_20_desc
        "wheel_spin" -> R.string.badge_wheel_spin_desc
        "pro_member" -> R.string.badge_pro_member_desc
        "early_bird" -> R.string.badge_early_bird_desc
        "night_owl" -> R.string.badge_night_owl_desc
        "squat_master" -> R.string.badge_squat_master_desc
        "hydration_champ" -> R.string.badge_hydration_champ_desc
        "breath_master" -> R.string.badge_breath_master_desc
        "level_10" -> R.string.badge_level_10_desc
        "level_25" -> R.string.badge_level_25_desc
        "level_50" -> R.string.badge_level_50_desc
        "social_connector" -> R.string.badge_social_connector_desc
        "consistency_king" -> R.string.badge_consistency_king_desc
        "farm_leader" -> R.string.badge_farm_leader_desc
        else -> R.string.badge_first_egg_desc
    }

    fun evaluate(profile: UserProfile, calorieLogsCount: Int): List<ChickenBadge> {
        val streakPeak = maxOf(profile.streakDays, profile.bestStreakDays)
        val hasScannedMeal = profile.mealScansCount >= 1 || calorieLogsCount >= 1
        val isTr = profile.appLanguage == "tr"
        return listOf(
            ChickenBadge(
                id = "first_egg",
                title = if (isTr) "İlk Yumurta" else "First Egg",
                description = if (isTr) "İlk antrenmanını başarıyla tamamla" else "Complete your first workout successfully",
                unlocked = profile.workoutsCompletedCount >= 1
            ),
            ChickenBadge(
                id = "flame_chick",
                title = if (isTr) "Ateşli Civciv" else "Flame Chick",
                description = if (isTr) "3 günlük antrenman serisi yakala" else "Maintain a 3-day workout streak",
                unlocked = streakPeak >= 3
            ),
            ChickenBadge(
                id = "smart_nutrition",
                title = if (isTr) "Akıllı Beslenme" else "Smart Nutrition",
                description = if (isTr) "İlk öğün kalori taramasını gerçekleştir" else "Scan or log your first meal",
                unlocked = hasScannedMeal
            ),
            ChickenBadge(
                id = "gym_bro_chicken",
                title = if (isTr) "Gym Bro Tavuk" else "Gym Bro Chicken",
                description = if (isTr) "Seviye 5 ve üzerine ulaş" else "Reach Level 5 and above",
                unlocked = profile.chickenLevel >= 5
            ),
            ChickenBadge(
                id = "iron_rooster",
                title = if (isTr) "Demir Horoz" else "Iron Rooster",
                description = if (isTr) "10 antrenman programını tamamla" else "Complete 10 workouts",
                unlocked = profile.workoutsCompletedCount >= 10
            ),
            ChickenBadge(
                id = "alpha_beast",
                title = if (isTr) "Alfa Canavar" else "Alpha Beast",
                description = if (isTr) "Seviye 20 efsanevi seviyesine ulaş" else "Reach Level 20 milestone",
                unlocked = profile.chickenLevel >= 20
            ),
            ChickenBadge(
                id = "streak_7",
                title = if (isTr) "7 Günlük Seri" else "7-Day Streak",
                description = if (isTr) "7 gün kesintisiz aktif ol" else "Stay active for 7 days in a row",
                unlocked = streakPeak >= 7
            ),
            ChickenBadge(
                id = "streak_14",
                title = if (isTr) "14 Günlük Seri" else "14-Day Streak",
                description = if (isTr) "14 gün disiplini elden bırakma" else "Maintain discipline for 14 days",
                unlocked = streakPeak >= 14
            ),
            ChickenBadge(
                id = "streak_30",
                title = if (isTr) "30 Günlük Efsane" else "30-Day Legend",
                description = if (isTr) "Tam 1 ay boyunca aralıksız devam et" else "Unstoppable 30-day streak",
                unlocked = streakPeak >= 30
            ),
            ChickenBadge(
                id = "workouts_25",
                title = if (isTr) "Antrenman Kıdemlisi" else "Workout Veteran",
                description = if (isTr) "25 antrenman seansını bitir" else "Finish 25 workout sessions",
                unlocked = profile.workoutsCompletedCount >= 25
            ),
            ChickenBadge(
                id = "workouts_50",
                title = if (isTr) "Demir Yumruk" else "Iron Fist",
                description = if (isTr) "50 antrenman seansını devir" else "Conquer 50 workout sessions",
                unlocked = profile.workoutsCompletedCount >= 50
            ),
            ChickenBadge(
                id = "workouts_100",
                title = if (isTr) "Yüzlük Efsane" else "Century Legend",
                description = if (isTr) "100 antrenmanla zirveye çık" else "Complete 100 workouts",
                unlocked = profile.workoutsCompletedCount >= 100
            ),
            ChickenBadge(
                id = "meals_5",
                title = if (isTr) "Kalori Avcısı" else "Calorie Hunter",
                description = if (isTr) "5 öğün kalori analizi yap" else "Log 5 meal scans",
                unlocked = profile.mealScansCount >= 5
            ),
            ChickenBadge(
                id = "meals_20",
                title = if (isTr) "Beslenme Uzmanı" else "Nutrition Expert",
                description = if (isTr) "20 öğün kaydı ve takibi yap" else "Log 20 meal scans",
                unlocked = profile.mealScansCount >= 20
            ),
            ChickenBadge(
                id = "wheel_spin",
                title = if (isTr) "Şanslı Çark" else "Lucky Wheeler",
                description = if (isTr) "Günlük XP çarkını başarıyla çevir" else "Spin the daily XP wheel",
                unlocked = profile.lastWheelSpinDate.isNotEmpty()
            ),
            ChickenBadge(
                id = "pro_member",
                title = if (isTr) "PRO Üye" else "PRO Member",
                description = if (isTr) "FitChicken PRO ayrıcalıklarını aktif et" else "Unlock PRO plan privileges",
                unlocked = profile.isProActive
            ),
            ChickenBadge(
                id = "early_bird",
                title = if (isTr) "Sabah Kuşu" else "Early Bird",
                description = if (isTr) "Erken saatlerde antrenman tamamla" else "Complete morning workouts",
                unlocked = profile.workoutsCompletedCount >= 3
            ),
            ChickenBadge(
                id = "night_owl",
                title = if (isTr) "Gece Avcısı" else "Night Owl",
                description = if (isTr) "Günün geç saatlerinde disiplini koru" else "Late evening workout dedication",
                unlocked = profile.workoutsCompletedCount >= 5
            ),
            ChickenBadge(
                id = "squat_master",
                title = if (isTr) "Squat Kralı" else "Squat Master",
                description = if (isTr) "Güç kapasiteni üst seviyeye taşı" else "Build elite squat capacity",
                unlocked = profile.squatCapacity == "35_plus" || profile.squatCapacity == "20_35"
            ),
            ChickenBadge(
                id = "hydration_champ",
                title = if (isTr) "Su Şampiyonu" else "Hydration Champ",
                description = if (isTr) "Günlük su ve protein görevini onayla" else "Confirm daily hydration and protein",
                unlocked = profile.questHydrationDone
            ),
            ChickenBadge(
                id = "breath_master",
                title = if (isTr) "Nefes Ustası" else "Breath Master",
                description = if (isTr) "Mobilizasyon ve nefes görevini bitir" else "Complete mobility and breath opener",
                unlocked = profile.questChallengeDone
            ),
            ChickenBadge(
                id = "level_10",
                title = if (isTr) "Seviye 10 Ustası" else "Level 10 Master",
                description = if (isTr) "Deneyim puanıyla 10. seviyeye tırman" else "Reach Level 10",
                unlocked = profile.chickenLevel >= 10
            ),
            ChickenBadge(
                id = "level_25",
                title = if (isTr) "Seviye 25 Şampiyonu" else "Level 25 Champion",
                description = if (isTr) "Deneyim puanıyla 25. seviyeye ulaş" else "Reach Level 25",
                unlocked = profile.chickenLevel >= 25
            ),
            ChickenBadge(
                id = "level_50",
                title = if (isTr) "Seviye 50 Zirve" else "Level 50 Apex",
                description = if (isTr) "FitChicken zirvesine adını yazdır" else "Reach Level 50 Apex",
                unlocked = profile.chickenLevel >= 50
            ),
            ChickenBadge(
                id = "social_connector",
                title = if (isTr) "Sosyal Tavuk" else "Social Connector",
                description = if (isTr) "Arkadaş davet kodunu paylaş" else "Share invite code with friends",
                unlocked = profile.referralCount >= 1
            ),
            ChickenBadge(
                id = "consistency_king",
                title = if (isTr) "Kararlılık Abidesi" else "Consistency King",
                description = if (isTr) "İstikrarlı antrenman rutini kur" else "Establish steady training routine",
                unlocked = streakPeak >= 5 && profile.workoutsCompletedCount >= 8
            ),
            ChickenBadge(
                id = "farm_leader",
                title = if (isTr) "Çiftlik Lideri" else "Farm Leader",
                description = if (isTr) "5 arkadaşına PRO ayrıcalığı kazandır" else "Invite 5 friends to unlock PRO",
                unlocked = profile.referralCount >= 5
            )
        )
    }
}

data class WeightEntry(
    val id: String,
    val weightKg: Double,
    val note: String,
    val loggedAtMillis: Long
)

data class CalendarWorkoutEntry(
    val id: String,
    val dateKey: String,
    val workoutId: String,
    val workoutTitle: String,
    val completed: Boolean,
    val durationMinutes: Int
)

data class AiChatMessage(
    val id: String,
    val isFromUser: Boolean,
    val text: String,
    val timestampMillis: Long = System.currentTimeMillis()
)
