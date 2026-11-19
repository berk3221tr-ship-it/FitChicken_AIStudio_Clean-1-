package com.example.data.catalog

internal enum class NutritionFeedbackScenario { EMPTY, TARGET_MET, OVER_BUDGET, PROTEIN_LOW, ON_TRACK }

internal data class NutritionFeedbackText(val title: String, val message: String)

/** Translated offline feedback copy used by the calorie tracker. */
internal object LocalizedNutritionCopy {
    private data class PairText(val title: String, val message: String)
    private val translations: Map<String, Map<NutritionFeedbackScenario, PairText>> = mapOf(
        "en" to mapOf(
            NutritionFeedbackScenario.EMPTY to PairText("FitChicken Fuel Check", "No meals logged yet. Add a food and its portion to track your {targetKcal} kcal and {targetProtein} g protein guide."),
            NutritionFeedbackScenario.TARGET_MET to PairText("Macro Sweet Spot Hit!", "Great work! You logged {consumedProtein}/{targetProtein} g protein and {consumedKcal} kcal. Keep recovery and balanced meals in focus."),
            NutritionFeedbackScenario.OVER_BUDGET to PairText("Above Calorie Guide", "You are above your {targetKcal} kcal guide ({consumedKcal} kcal logged). Return to your usual balanced routine; no need for extreme compensation."),
            NutritionFeedbackScenario.PROTEIN_LOW to PairText("Protein Check-In", "You have logged {consumedKcal} kcal and {consumedProtein} g protein. Add a balanced protein source to move toward {targetProtein} g."),
            NutritionFeedbackScenario.ON_TRACK to PairText("Good Momentum Today!", "You logged {consumedKcal} kcal and {consumedProtein} g protein. Remaining guide: {remainingKcal} kcal and {remainingProtein} g protein. Keep it balanced!")
        ),
        "tr" to mapOf(
            NutritionFeedbackScenario.EMPTY to PairText("FitChicken Beslenme Takibi", "Henüz öğün eklemedin. Yediğin besini ve porsiyonunu ekleyerek {targetKcal} kcal ve {targetProtein} g protein hedefini takip et."),
            NutritionFeedbackScenario.TARGET_MET to PairText("Makro Dengesi Harika!", "Harika! {consumedProtein}/{targetProtein} g protein ve {consumedKcal} kcal kaydettin. Dengeli beslenmeye ve toparlanmaya odaklan."),
            NutritionFeedbackScenario.OVER_BUDGET to PairText("Kalori Hedefinin Üzerinde", "{targetKcal} kcal hedefinin üzerindesin ({consumedKcal} kcal). Normal ve dengeli rutinine dön; bunu aşırı kısıtlamayla telafi etmene gerek yok."),
            NutritionFeedbackScenario.PROTEIN_LOW to PairText("Protein Takibi", "Şu ana kadar {consumedKcal} kcal ve {consumedProtein} g protein kaydettin. {targetProtein} g hedefine yaklaşmak için dengeli bir protein kaynağı ekleyebilirsin."),
            NutritionFeedbackScenario.ON_TRACK to PairText("Bugün İyi İlerliyorsun!", "{consumedKcal} kcal ve {consumedProtein} g protein kaydettin. Kalan hedef: {remainingKcal} kcal ve {remainingProtein} g protein. Dengeli devam et!")
        ),
        "de" to mapOf(
            NutritionFeedbackScenario.EMPTY to PairText("FitChicken-Ernährungscheck", "Noch keine Mahlzeit eingetragen. Füge ein Lebensmittel und die Portion hinzu, um deinen Richtwert von {targetKcal} kcal und {targetProtein} g Protein zu verfolgen."),
            NutritionFeedbackScenario.TARGET_MET to PairText("Makro-Balance erreicht!", "Gut gemacht! Du hast {consumedProtein}/{targetProtein} g Protein und {consumedKcal} kcal eingetragen. Achte weiter auf ausgewogene Mahlzeiten und Erholung."),
            NutritionFeedbackScenario.OVER_BUDGET to PairText("Über dem Kalorien-Richtwert", "Du liegst über deinem Richtwert von {targetKcal} kcal ({consumedKcal} kcal). Kehre zu deiner ausgewogenen Routine zurück; extreme Ausgleichsmaßnahmen sind nicht nötig."),
            NutritionFeedbackScenario.PROTEIN_LOW to PairText("Protein-Check", "Du hast bisher {consumedKcal} kcal und {consumedProtein} g Protein eingetragen. Ergänze eine ausgewogene Proteinquelle, um dich {targetProtein} g anzunähern."),
            NutritionFeedbackScenario.ON_TRACK to PairText("Guter Fortschritt heute!", "Du hast {consumedKcal} kcal und {consumedProtein} g Protein eingetragen. Übrig: {remainingKcal} kcal und {remainingProtein} g Protein. Bleib ausgewogen!")
        ),
        "fr" to mapOf(
            NutritionFeedbackScenario.EMPTY to PairText("Suivi nutrition FitChicken", "Aucun repas enregistré. Ajoute un aliment et sa portion pour suivre ton repère de {targetKcal} kcal et {targetProtein} g de protéines."),
            NutritionFeedbackScenario.TARGET_MET to PairText("Bon équilibre des macros !", "Bravo ! Tu as enregistré {consumedProtein}/{targetProtein} g de protéines et {consumedKcal} kcal. Continue à privilégier l'équilibre et la récupération."),
            NutritionFeedbackScenario.OVER_BUDGET to PairText("Au-dessus du repère calorique", "Tu dépasses ton repère de {targetKcal} kcal ({consumedKcal} kcal). Reprends simplement ta routine équilibrée, sans compensation extrême."),
            NutritionFeedbackScenario.PROTEIN_LOW to PairText("Point protéines", "Tu as enregistré {consumedKcal} kcal et {consumedProtein} g de protéines. Ajoute une source de protéines équilibrée pour te rapprocher de {targetProtein} g."),
            NutritionFeedbackScenario.ON_TRACK to PairText("Bonne progression aujourd'hui !", "Tu as enregistré {consumedKcal} kcal et {consumedProtein} g de protéines. Repère restant : {remainingKcal} kcal et {remainingProtein} g de protéines. Garde l'équilibre !")
        ),
        "es" to mapOf(
            NutritionFeedbackScenario.EMPTY to PairText("Seguimiento nutricional FitChicken", "Aún no has registrado comidas. Añade un alimento y su porción para seguir la referencia de {targetKcal} kcal y {targetProtein} g de proteína."),
            NutritionFeedbackScenario.TARGET_MET to PairText("¡Buen equilibrio de macros!", "¡Buen trabajo! Has registrado {consumedProtein}/{targetProtein} g de proteína y {consumedKcal} kcal. Sigue priorizando el equilibrio y la recuperación."),
            NutritionFeedbackScenario.OVER_BUDGET to PairText("Por encima de la referencia calórica", "Superas tu referencia de {targetKcal} kcal ({consumedKcal} kcal). Vuelve a tu rutina equilibrada habitual; no hace falta compensar de forma extrema."),
            NutritionFeedbackScenario.PROTEIN_LOW to PairText("Revisión de proteína", "Has registrado {consumedKcal} kcal y {consumedProtein} g de proteína. Añade una fuente equilibrada de proteína para acercarte a {targetProtein} g."),
            NutritionFeedbackScenario.ON_TRACK to PairText("¡Buen progreso hoy!", "Has registrado {consumedKcal} kcal y {consumedProtein} g de proteína. Referencia restante: {remainingKcal} kcal y {remainingProtein} g de proteína. ¡Mantén el equilibrio!")
        ),
        "it" to mapOf(
            NutritionFeedbackScenario.EMPTY to PairText("Monitoraggio FitChicken", "Non hai ancora registrato pasti. Aggiungi un alimento e la porzione per seguire il riferimento di {targetKcal} kcal e {targetProtein} g di proteine."),
            NutritionFeedbackScenario.TARGET_MET to PairText("Ottimo equilibrio dei macronutrienti!", "Ben fatto! Hai registrato {consumedProtein}/{targetProtein} g di proteine e {consumedKcal} kcal. Continua a dare priorità a equilibrio e recupero."),
            NutritionFeedbackScenario.OVER_BUDGET to PairText("Sopra il riferimento calorico", "Hai superato il riferimento di {targetKcal} kcal ({consumedKcal} kcal). Torna alla tua normale routine equilibrata, senza compensazioni estreme."),
            NutritionFeedbackScenario.PROTEIN_LOW to PairText("Controllo delle proteine", "Hai registrato {consumedKcal} kcal e {consumedProtein} g di proteine. Aggiungi una fonte proteica equilibrata per avvicinarti a {targetProtein} g."),
            NutritionFeedbackScenario.ON_TRACK to PairText("Oggi stai andando bene!", "Hai registrato {consumedKcal} kcal e {consumedProtein} g di proteine. Riferimento rimanente: {remainingKcal} kcal e {remainingProtein} g di proteine. Mantieni l'equilibrio!")
        ),
        "ru" to mapOf(
            NutritionFeedbackScenario.EMPTY to PairText("Питание FitChicken", "Приёмы пищи ещё не записаны. Добавь продукт и порцию, чтобы отслеживать ориентир {targetKcal} ккал и {targetProtein} г белка."),
            NutritionFeedbackScenario.TARGET_MET to PairText("Баланс макронутриентов достигнут!", "Отлично! Записано {consumedProtein}/{targetProtein} г белка и {consumedKcal} ккал. Продолжай уделять внимание сбалансированному питанию и восстановлению."),
            NutritionFeedbackScenario.OVER_BUDGET to PairText("Выше ориентира по калориям", "Ты превысил ориентир {targetKcal} ккал ({consumedKcal} ккал). Просто вернись к обычному сбалансированному режиму — крайние ограничения не нужны."),
            NutritionFeedbackScenario.PROTEIN_LOW to PairText("Проверка белка", "Записано {consumedKcal} ккал и {consumedProtein} г белка. Добавь сбалансированный источник белка, чтобы приблизиться к {targetProtein} г."),
            NutritionFeedbackScenario.ON_TRACK to PairText("Хороший прогресс сегодня!", "Записано {consumedKcal} ккал и {consumedProtein} г белка. Остаток ориентира: {remainingKcal} ккал и {remainingProtein} г белка. Сохраняй баланс!")
        ),
        "nl" to mapOf(
            NutritionFeedbackScenario.EMPTY to PairText("FitChicken-voedingscheck", "Je hebt nog geen maaltijden geregistreerd. Voeg een product en portie toe om je richtlijn van {targetKcal} kcal en {targetProtein} g eiwit te volgen."),
            NutritionFeedbackScenario.TARGET_MET to PairText("Goede macrobalans!", "Goed gedaan! Je hebt {consumedProtein}/{targetProtein} g eiwit en {consumedKcal} kcal geregistreerd. Blijf letten op evenwichtige maaltijden en herstel."),
            NutritionFeedbackScenario.OVER_BUDGET to PairText("Boven de calorierichtlijn", "Je zit boven je richtlijn van {targetKcal} kcal ({consumedKcal} kcal). Ga terug naar je gewone evenwichtige routine; extreme compensatie is niet nodig."),
            NutritionFeedbackScenario.PROTEIN_LOW to PairText("Eiwitcheck", "Je hebt {consumedKcal} kcal en {consumedProtein} g eiwit geregistreerd. Voeg een evenwichtige eiwitbron toe om dichter bij {targetProtein} g te komen."),
            NutritionFeedbackScenario.ON_TRACK to PairText("Goede voortgang vandaag!", "Je hebt {consumedKcal} kcal en {consumedProtein} g eiwit geregistreerd. Resterende richtlijn: {remainingKcal} kcal en {remainingProtein} g eiwit. Houd het in balans!")
        ),
        "pl" to mapOf(
            NutritionFeedbackScenario.EMPTY to PairText("Kontrola żywienia FitChicken", "Nie zapisano jeszcze posiłków. Dodaj produkt i porcję, aby śledzić orientacyjny cel {targetKcal} kcal i {targetProtein} g białka."),
            NutritionFeedbackScenario.TARGET_MET to PairText("Świetny bilans makroskładników!", "Dobra robota! Zapisano {consumedProtein}/{targetProtein} g białka i {consumedKcal} kcal. Nadal stawiaj na zbilansowane posiłki i regenerację."),
            NutritionFeedbackScenario.OVER_BUDGET to PairText("Powyżej celu kalorii", "Przekraczasz orientacyjny cel {targetKcal} kcal ({consumedKcal} kcal). Wróć do zwykłej, zbilansowanej rutyny — nie stosuj skrajnej rekompensaty."),
            NutritionFeedbackScenario.PROTEIN_LOW to PairText("Kontrola białka", "Zapisano {consumedKcal} kcal i {consumedProtein} g białka. Dodaj zbilansowane źródło białka, aby zbliżyć się do {targetProtein} g."),
            NutritionFeedbackScenario.ON_TRACK to PairText("Dobry postęp dzisiaj!", "Zapisano {consumedKcal} kcal i {consumedProtein} g białka. Pozostały cel orientacyjny: {remainingKcal} kcal i {remainingProtein} g białka. Zachowaj równowagę!")
        ),
        "pt" to mapOf(
            NutritionFeedbackScenario.EMPTY to PairText("Acompanhamento FitChicken", "Ainda não registaste refeições. Adiciona um alimento e a porção para acompanhar a referência de {targetKcal} kcal e {targetProtein} g de proteína."),
            NutritionFeedbackScenario.TARGET_MET to PairText("Excelente equilíbrio de macros!", "Muito bem! Registaste {consumedProtein}/{targetProtein} g de proteína e {consumedKcal} kcal. Continua a dar prioridade ao equilíbrio e à recuperação."),
            NutritionFeedbackScenario.OVER_BUDGET to PairText("Acima da referência de calorias", "Estás acima da referência de {targetKcal} kcal ({consumedKcal} kcal). Retoma a tua rotina equilibrada habitual; não precisas de compensações extremas."),
            NutritionFeedbackScenario.PROTEIN_LOW to PairText("Verificação de proteína", "Registaste {consumedKcal} kcal e {consumedProtein} g de proteína. Adiciona uma fonte equilibrada de proteína para te aproximares de {targetProtein} g."),
            NutritionFeedbackScenario.ON_TRACK to PairText("Bom progresso hoje!", "Registaste {consumedKcal} kcal e {consumedProtein} g de proteína. Referência restante: {remainingKcal} kcal e {remainingProtein} g de proteína. Mantém o equilíbrio!")
        )
    )

    fun get(languageCode: String, scenario: NutritionFeedbackScenario, values: Map<String, String>): NutritionFeedbackText {
        val template = translations[languageCode.lowercase(java.util.Locale.ROOT)]?.get(scenario)
            ?: translations.getValue("en").getValue(scenario)
        fun applyTokens(input: String): String = values.entries.fold(input) { text, (key, value) ->
            text.replace("{$key}", value)
        }
        return NutritionFeedbackText(applyTokens(template.title), applyTokens(template.message))
    }
}
