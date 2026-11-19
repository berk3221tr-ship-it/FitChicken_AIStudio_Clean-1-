package com.example.data.catalog

import android.content.Context
import androidx.compose.runtime.Composable
import com.example.R
import com.example.data.model.ExerciseItem
import com.example.data.model.WorkoutProgram
import com.example.ui.i18n.LocalLocalizedContext

private fun isTurkish(context: Context): Boolean {
    val lang = context.resources.configuration.locales.get(0)?.language ?: ""
    return lang.equals("tr", ignoreCase = true)
}

private fun lookupString(context: Context, resName: String, fallback: String): String {
    val resId = context.resources.getIdentifier(resName, "string", context.packageName)
    return if (resId != 0) context.getString(resId) else fallback
}

private fun lookupStringArray(context: Context, resName: String, fallback: List<String>): List<String> {
    val resId = context.resources.getIdentifier(resName, "array", context.packageName)
    if (resId == 0) return fallback
    val arr = context.resources.getStringArray(resId)
    return if (arr.isNotEmpty()) arr.toList() else fallback
}

fun WorkoutProgram.localizedTitle(context: Context): String {
    val fallback = if (isTurkish(context) && titleTr.isNotBlank()) titleTr else title
    return lookupString(context, "w_${id}_title", fallback)
}

fun WorkoutProgram.localizedSubtitle(context: Context): String {
    val fallback = if (isTurkish(context) && subtitleTr.isNotBlank()) subtitleTr else subtitle
    return lookupString(context, "w_${id}_sub", fallback)
}

fun WorkoutProgram.localizedDifficulty(context: Context): String = when (difficulty.trim()) {
    "Başlangıç", "Beginner" -> context.getString(R.string.diff_beginner)
    "Orta", "Intermediate" -> context.getString(R.string.diff_intermediate)
    "Zor", "Advanced" -> context.getString(R.string.diff_advanced)
    "İleri", "PRO", "Pro" -> context.getString(R.string.diff_pro)
    else -> difficulty
}

fun ExerciseItem.localizedName(context: Context): String {
    val fallback = if (isTurkish(context) && nameTr.isNotBlank()) nameTr else name
    return lookupString(context, "ex_${id}_name", fallback)
}

fun ExerciseItem.localizedTargetMuscle(context: Context): String {
    val fallback = if (isTurkish(context) && targetMuscleTr.isNotBlank()) targetMuscleTr else targetMuscle
    return lookupString(context, "ex_${id}_muscle", fallback)
}

fun ExerciseItem.localizedSecondaryMuscles(context: Context): String {
    if (isTurkish(context) && secondaryMusclesTr.isNotBlank()) return secondaryMusclesTr
    val language = context.resources.configuration.locales.get(0)?.language?.lowercase() ?: "en"
    if (language == "en" || secondaryMuscles.isBlank()) return secondaryMuscles
    val words = secondaryMuscles.split(Regex("\\s*,\\s*"))
    val dictionary = secondaryMuscleDictionary[language] ?: return secondaryMuscles
    return words.joinToString(", ") { original ->
        dictionary[original.trim().lowercase(java.util.Locale.ROOT)] ?: original.trim()
    }
}

private val secondaryMuscleDictionary: Map<String, Map<String, String>> = mapOf(
    "de" to mapOf("obliques" to "Schräge Bauchmuskeln", "lower back" to "Unterer Rücken", "shoulders" to "Schultern", "front delts" to "Vordere Schultern", "rear delts" to "Hintere Schultern", "triceps" to "Trizeps", "biceps" to "Bizeps", "upper chest" to "Obere Brust", "core" to "Rumpf", "glutes" to "Gesäß", "quadriceps" to "Quadrizeps", "hamstrings" to "Beinbeuger", "calves" to "Waden", "forearms" to "Unterarme", "lats" to "Breiter Rückenmuskel", "traps" to "Trapezmuskel"),
    "fr" to mapOf("obliques" to "Obliques", "lower back" to "Bas du dos", "shoulders" to "Épaules", "front delts" to "Deltoïdes antérieurs", "rear delts" to "Deltoïdes postérieurs", "triceps" to "Triceps", "biceps" to "Biceps", "upper chest" to "Haut des pectoraux", "core" to "Sangle abdominale", "glutes" to "Fessiers", "quadriceps" to "Quadriceps", "hamstrings" to "Ischio-jambiers", "calves" to "Mollets", "forearms" to "Avant-bras", "lats" to "Grand dorsal", "traps" to "Trapèzes"),
    "es" to mapOf("obliques" to "Oblicuos", "lower back" to "Zona lumbar", "shoulders" to "Hombros", "front delts" to "Deltoides anteriores", "rear delts" to "Deltoides posteriores", "triceps" to "Tríceps", "biceps" to "Bíceps", "upper chest" to "Pecho superior", "core" to "Zona media", "glutes" to "Glúteos", "quadriceps" to "Cuádriceps", "hamstrings" to "Isquiotibiales", "calves" to "Pantorrillas", "forearms" to "Antebrazos", "lats" to "Dorsales", "traps" to "Trapecios"),
    "it" to mapOf("obliques" to "Obliqui", "lower back" to "Zona lombare", "shoulders" to "Spalle", "front delts" to "Deltoidi anteriori", "rear delts" to "Deltoidi posteriori", "triceps" to "Tricipiti", "biceps" to "Bicipiti", "upper chest" to "Parte alta del petto", "core" to "Core", "glutes" to "Glutei", "quadriceps" to "Quadricipiti", "hamstrings" to "Femorali", "calves" to "Polpacci", "forearms" to "Avambracci", "lats" to "Gran dorsale", "traps" to "Trapezi"),
    "ru" to mapOf("obliques" to "Косые мышцы живота", "lower back" to "Поясница", "shoulders" to "Плечи", "front delts" to "Передние дельты", "rear delts" to "Задние дельты", "triceps" to "Трицепсы", "biceps" to "Бицепсы", "upper chest" to "Верх груди", "core" to "Мышцы корпуса", "glutes" to "Ягодицы", "quadriceps" to "Квадрицепсы", "hamstrings" to "Задняя поверхность бедра", "calves" to "Икры", "forearms" to "Предплечья", "lats" to "Широчайшие мышцы", "traps" to "Трапеции"),
    "nl" to mapOf("obliques" to "Schuine buikspieren", "lower back" to "Onderrug", "shoulders" to "Schouders", "front delts" to "Voorste schouders", "rear delts" to "Achterste schouders", "triceps" to "Triceps", "biceps" to "Biceps", "upper chest" to "Bovenborst", "core" to "Romp", "glutes" to "Bilspieren", "quadriceps" to "Quadriceps", "hamstrings" to "Hamstrings", "calves" to "Kuiten", "forearms" to "Onderarmen", "lats" to "Brede rugspieren", "traps" to "Trapezius"),
    "pl" to mapOf("obliques" to "Mięśnie skośne brzucha", "lower back" to "Dolna część pleców", "shoulders" to "Barki", "front delts" to "Przednie aktony barków", "rear delts" to "Tylne aktony barków", "triceps" to "Triceps", "biceps" to "Biceps", "upper chest" to "Górna część klatki", "core" to "Mięśnie głębokie", "glutes" to "Pośladki", "quadriceps" to "Czworogłowe uda", "hamstrings" to "Dwugłowe uda", "calves" to "Łydki", "forearms" to "Przedramiona", "lats" to "Najszersze grzbietu", "traps" to "Czworoboczne"),
    "pt" to mapOf("obliques" to "Oblíquos", "lower back" to "Lombar", "shoulders" to "Ombros", "front delts" to "Deltoides anteriores", "rear delts" to "Deltoides posteriores", "triceps" to "Tríceps", "biceps" to "Bíceps", "upper chest" to "Parte superior do peito", "core" to "Região abdominal", "glutes" to "Glúteos", "quadriceps" to "Quadríceps", "hamstrings" to "Isquiotibiais", "calves" to "Gémeos", "forearms" to "Antebraços", "lats" to "Grande dorsal", "traps" to "Trapézios")
)

fun ExerciseItem.localizedSetsReps(context: Context): String =
    lookupString(context, "ex_${id}_sets", setsReps)

fun ExerciseItem.localizedSteps(context: Context): List<String> {
    val fallback = if (isTurkish(context) && stepsTr.isNotEmpty()) stepsTr else steps
    return lookupStringArray(context, "ex_${id}_steps", fallback)
}

fun ExerciseItem.localizedFormCue(context: Context): String {
    val fallback = if (isTurkish(context) && formCueTr.isNotBlank()) formCueTr else formCue
    return lookupString(context, "ex_${id}_cue", fallback)
}

fun ExerciseItem.localizedBreathingCue(context: Context): String {
    val fallback = if (isTurkish(context) && breathingCueTr.isNotBlank()) breathingCueTr else breathingCue
    return lookupString(context, "ex_${id}_breath", fallback)
}

fun ExerciseItem.localizedCommonMistake(context: Context): String {
    val fallback = if (isTurkish(context) && commonMistakeTr.isNotBlank()) commonMistakeTr else commonMistake
    return lookupString(context, "ex_${id}_mistake", fallback)
}

@Composable
fun WorkoutProgram.localizedTitle(): String = localizedTitle(LocalLocalizedContext.current)

@Composable
fun WorkoutProgram.localizedSubtitle(): String = localizedSubtitle(LocalLocalizedContext.current)

@Composable
fun WorkoutProgram.localizedDifficulty(): String = localizedDifficulty(LocalLocalizedContext.current)

@Composable
fun ExerciseItem.localizedName(): String = localizedName(LocalLocalizedContext.current)

@Composable
fun ExerciseItem.localizedTargetMuscle(): String = localizedTargetMuscle(LocalLocalizedContext.current)

@Composable
fun ExerciseItem.localizedSecondaryMuscles(): String = localizedSecondaryMuscles(LocalLocalizedContext.current)

@Composable
fun ExerciseItem.localizedSetsReps(): String = localizedSetsReps(LocalLocalizedContext.current)

@Composable
fun ExerciseItem.localizedSteps(): List<String> = localizedSteps(LocalLocalizedContext.current)

@Composable
fun ExerciseItem.localizedFormCue(): String = localizedFormCue(LocalLocalizedContext.current)

@Composable
fun ExerciseItem.localizedBreathingCue(): String = localizedBreathingCue(LocalLocalizedContext.current)

@Composable
fun ExerciseItem.localizedCommonMistake(): String = localizedCommonMistake(LocalLocalizedContext.current)
