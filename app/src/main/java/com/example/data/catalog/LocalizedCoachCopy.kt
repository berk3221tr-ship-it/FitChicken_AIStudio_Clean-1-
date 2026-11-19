package com.example.data.catalog

/** Offline translations for the local coach in all supported app languages. */
internal object LocalizedCoachCopy {
    private val topics: Map<String, Map<CoachTopic, String>> = mapOf(
        "de" to mapOf(
            CoachTopic.GREETING to "Hallo {NAME}! Ich bin FitChicken. Frag mich nach Training, Ernährung, Haltung oder Erholung.",
            CoachTopic.PROTEIN_NUTRITION to "Für {NAME}: Verteile etwa {PROTEIN_G} g Protein auf 3–4 Mahlzeiten. Gute Quellen sind Eier, Joghurt, Hülsenfrüchte, Fisch und mageres Fleisch.",
            CoachTopic.FAT_LOSS to "Für nachhaltige Fortschritte, {NAME}: Kombiniere regelmäßige Bewegung, Krafttraining und ausgewogene Mahlzeiten. Vermeide extreme Einschränkungen und schlafe ausreichend.",
            CoachTopic.MUSCLE_GAIN to "Dein Muskelaufbauplan, {NAME}: Steigere Wiederholungen oder Gewicht schrittweise, achte auf saubere Technik und gönne deinem Körper Erholung.",
            CoachTopic.CHEST_PUSH to "Für Brustübungen, {NAME}: Halte die Schulterblätter stabil, kontrolliere das Absenken und wähle ein Gewicht, das du sauber bewegen kannst.",
            CoachTopic.ARMS_SHOULDERS to "Für Arme und Schultern, {NAME}: Bewege das Gewicht kontrolliert, halte die Ellbogen stabil und vermeide Schwung aus dem Rücken.",
            CoachTopic.BACK_POSTURE to "Für einen starken Rücken, {NAME}: Ziehe bei Ruderbewegungen mit den Ellbogen, halte den Rumpf stabil und lege regelmäßige Bewegungspausen ein.",
            CoachTopic.LEGS_GLUTES to "Beim Beintraining, {NAME}: Halte Knie und Füße in einer stabilen Linie, kontrolliere jede Wiederholung und setze Technik vor Gewicht.",
            CoachTopic.ABS_CORE to "Für deine Rumpfmuskulatur, {NAME}: Spanne den Bauch an, halte die Wirbelsäule neutral und atme gleichmäßig. Kontrolle ist wichtiger als Tempo.",
            CoachTopic.CARDIO_HIIT to "Für Ausdauer, {NAME}: Beginne in einem Tempo, bei dem du noch sprechen kannst. Steigere Dauer und Intensität langsam und plane Erholung ein.",
            CoachTopic.YOUTH_GROWTH to "Für Wachstum und Haltung, {NAME}: Schlaf, ausgewogene Ernährung, tägliche Bewegung und altersgerechtes Training sind wichtig. Übungen garantieren keine zusätzliche Körpergröße.",
            CoachTopic.RECOVERY_SLEEP to "Für bessere Regeneration, {NAME}: Halte möglichst feste Schlafzeiten ein, reduziere helles Bildschirmlicht vor dem Schlafen und plane Ruhetage.",
            CoachTopic.HYDRATION_SUPPS to "Für Flüssigkeit und Supplements, {NAME}: Trinke regelmäßig passend zu Durst und Aktivität. Supplements ersetzen keine ausgewogene Ernährung; prüfe Produkte und Dosierungen.",
            CoachTopic.MOTIVATION_PLATEAU to "Wenn du feststeckst, {NAME}: Vergleiche deine Entwicklung über mehrere Wochen. Ändere nur eine Sache auf einmal und gib dem Plan Zeit.",
            CoachTopic.GENERAL_PLAN to "Guter Start, {NAME}: Plane 2–4 passende Einheiten pro Woche, bewege dich an Ruhetagen locker und notiere deine Fortschritte. Beständigkeit zählt."
        ),
        "fr" to mapOf(
            CoachTopic.GREETING to "Bonjour {NAME} ! Je suis FitChicken. Pose-moi tes questions sur l'entraînement, la nutrition, la posture ou la récupération.",
            CoachTopic.PROTEIN_NUTRITION to "Pour {NAME} : répartis environ {PROTEIN_G} g de protéines sur 3–4 repas. Pense aux œufs, yaourts, légumineuses, poissons et viandes maigres.",
            CoachTopic.FAT_LOSS to "Pour progresser durablement, {NAME} : associe activité régulière, renforcement musculaire et repas équilibrés. Évite les restrictions extrêmes et dors suffisamment.",
            CoachTopic.MUSCLE_GAIN to "Ton plan musculaire, {NAME} : augmente progressivement les répétitions ou la charge, garde une bonne technique et prévois de la récupération.",
            CoachTopic.CHEST_PUSH to "Pour la poitrine, {NAME} : stabilise les omoplates, contrôle la descente et choisis une charge que tu peux déplacer correctement.",
            CoachTopic.ARMS_SHOULDERS to "Pour les bras et les épaules, {NAME} : contrôle le mouvement, garde les coudes stables et évite l'élan du dos.",
            CoachTopic.BACK_POSTURE to "Pour renforcer ton dos, {NAME} : tire avec les coudes, garde le tronc stable et prends régulièrement des pauses actives.",
            CoachTopic.LEGS_GLUTES to "Pour les jambes, {NAME} : aligne les genoux avec les pieds, contrôle chaque répétition et privilégie la technique à la charge.",
            CoachTopic.ABS_CORE to "Pour le gainage, {NAME} : contracte les abdominaux, garde la colonne neutre et respire régulièrement. Le contrôle vaut mieux que la vitesse.",
            CoachTopic.CARDIO_HIIT to "Pour le cardio, {NAME} : commence à une allure qui te permet encore de parler. Augmente progressivement la durée ou l'intensité et récupère suffisamment.",
            CoachTopic.YOUTH_GROWTH to "Pour la croissance et la posture, {NAME} : sommeil, alimentation variée, activité quotidienne et entraînement adapté à l'âge sont essentiels. L'exercice ne garantit pas une taille supplémentaire.",
            CoachTopic.RECOVERY_SLEEP to "Pour mieux récupérer, {NAME} : garde des horaires de sommeil réguliers, limite les écrans lumineux avant le coucher et prévois des jours de repos.",
            CoachTopic.HYDRATION_SUPPS to "Pour l'hydratation, {NAME} : bois régulièrement selon ta soif et ton activité. Les compléments ne remplacent pas une alimentation équilibrée ; vérifie les doses.",
            CoachTopic.MOTIVATION_PLATEAU to "Si tu stagnes, {NAME} : observe tes progrès sur plusieurs semaines. Modifie un seul élément à la fois et laisse du temps à ton programme.",
            CoachTopic.GENERAL_PLAN to "Pour commencer, {NAME} : prévois 2–4 séances adaptées par semaine, bouge doucement les jours de repos et note tes progrès. La régularité compte."
        ),
        "es" to mapOf(
            CoachTopic.GREETING to "¡Hola, {NAME}! Soy FitChicken. Pregúntame sobre entrenamiento, alimentación, postura o recuperación.",
            CoachTopic.PROTEIN_NUTRITION to "Para {NAME}: reparte unos {PROTEIN_G} g de proteína entre 3–4 comidas. Elige huevos, yogur, legumbres, pescado y carnes magras.",
            CoachTopic.FAT_LOSS to "Para progresar de forma sostenible, {NAME}: combina actividad regular, fuerza y comidas equilibradas. Evita restricciones extremas y duerme bien.",
            CoachTopic.MUSCLE_GAIN to "Tu guía muscular, {NAME}: aumenta poco a poco las repeticiones o el peso, mantén una buena técnica y reserva tiempo para recuperarte.",
            CoachTopic.CHEST_PUSH to "Para entrenar el pecho, {NAME}: estabiliza los omóplatos, controla la bajada y usa una carga que puedas mover con buena técnica.",
            CoachTopic.ARMS_SHOULDERS to "Para brazos y hombros, {NAME}: controla el movimiento, mantén los codos estables y no uses impulso de la espalda.",
            CoachTopic.BACK_POSTURE to "Para fortalecer la espalda, {NAME}: dirige los remos con los codos, mantén firme el tronco y haz pausas activas.",
            CoachTopic.LEGS_GLUTES to "En piernas, {NAME}: mantén las rodillas alineadas con los pies, controla cada repetición y prioriza la técnica sobre el peso.",
            CoachTopic.ABS_CORE to "Para el abdomen, {NAME}: activa la zona media, conserva la columna neutra y respira con regularidad. El control importa más que la velocidad.",
            CoachTopic.CARDIO_HIIT to "Para mejorar la resistencia, {NAME}: empieza a un ritmo que te permita hablar. Aumenta el tiempo o la intensidad poco a poco y recupérate bien.",
            CoachTopic.YOUTH_GROWTH to "Para crecimiento y postura, {NAME}: dormir bien, comer variado, moverse a diario y entrenar según la edad es fundamental. El ejercicio no garantiza crecer más.",
            CoachTopic.RECOVERY_SLEEP to "Para recuperarte mejor, {NAME}: mantén horarios de sueño regulares, reduce las pantallas antes de dormir y alterna esfuerzo con descanso.",
            CoachTopic.HYDRATION_SUPPS to "Para hidratarte, {NAME}: bebe según tu sed y actividad. Los suplementos no sustituyen una dieta equilibrada; revisa los productos y las dosis.",
            CoachTopic.MOTIVATION_PLATEAU to "Si te estancas, {NAME}: evalúa tu progreso durante varias semanas. Cambia una sola variable cada vez y dale tiempo al plan.",
            CoachTopic.GENERAL_PLAN to "Buen comienzo, {NAME}: programa 2–4 sesiones adecuadas por semana, mantén actividad suave en los días de descanso y registra tus avances."
        ),
        "it" to mapOf(
            CoachTopic.GREETING to "Ciao {NAME}! Sono FitChicken. Chiedimi di allenamento, alimentazione, postura o recupero.",
            CoachTopic.PROTEIN_NUTRITION to "Per {NAME}: distribuisci circa {PROTEIN_G} g di proteine in 3–4 pasti. Scegli uova, yogurt, legumi, pesce e carni magre.",
            CoachTopic.FAT_LOSS to "Per migliorare in modo sostenibile, {NAME}: abbina attività regolare, forza e pasti equilibrati. Evita restrizioni estreme e cura il sonno.",
            CoachTopic.MUSCLE_GAIN to "Il tuo piano muscolare, {NAME}: aumenta gradualmente ripetizioni o carico, mantieni una tecnica precisa e lascia spazio al recupero.",
            CoachTopic.CHEST_PUSH to "Per il petto, {NAME}: stabilizza le scapole, controlla la discesa e scegli un carico che puoi gestire con buona tecnica.",
            CoachTopic.ARMS_SHOULDERS to "Per braccia e spalle, {NAME}: controlla il movimento, mantieni stabili i gomiti ed evita lo slancio della schiena.",
            CoachTopic.BACK_POSTURE to "Per rinforzare la schiena, {NAME}: guida le tirate con i gomiti, mantieni stabile il busto e fai pause attive.",
            CoachTopic.LEGS_GLUTES to "Per le gambe, {NAME}: mantieni le ginocchia in linea con i piedi, controlla ogni ripetizione e privilegia la tecnica al carico.",
            CoachTopic.ABS_CORE to "Per il core, {NAME}: attiva l'addome, mantieni la colonna neutra e respira regolarmente. Il controllo conta più della velocità.",
            CoachTopic.CARDIO_HIIT to "Per la resistenza, {NAME}: inizia a un ritmo che ti consenta di parlare. Aumenta gradualmente durata o intensità e recupera a sufficienza.",
            CoachTopic.YOUTH_GROWTH to "Per crescita e postura, {NAME}: sonno, alimentazione varia, movimento quotidiano e allenamento adatto all'età sono fondamentali. L'esercizio non garantisce più altezza.",
            CoachTopic.RECOVERY_SLEEP to "Per recuperare meglio, {NAME}: mantieni orari di sonno regolari, limita gli schermi prima di dormire e programma giorni di recupero.",
            CoachTopic.HYDRATION_SUPPS to "Per idratarti, {NAME}: bevi regolarmente in base a sete e attività. Gli integratori non sostituiscono una dieta equilibrata; controlla le dosi.",
            CoachTopic.MOTIVATION_PLATEAU to "Se sei in stallo, {NAME}: valuta i progressi su più settimane. Modifica una sola cosa alla volta e dai tempo al programma.",
            CoachTopic.GENERAL_PLAN to "Per iniziare, {NAME}: programma 2–4 sessioni adatte a settimana, muoviti leggermente nei giorni di riposo e registra i progressi."
        ),
        "ru" to mapOf(
            CoachTopic.GREETING to "Привет, {NAME}! Я FitChicken. Спрашивай о тренировках, питании, осанке или восстановлении.",
            CoachTopic.PROTEIN_NUTRITION to "Для {NAME}: распределяй около {PROTEIN_G} г белка на 3–4 приёма пищи. Подойдут яйца, йогурт, бобовые, рыба и нежирное мясо.",
            CoachTopic.FAT_LOSS to "Для устойчивого прогресса, {NAME}: сочетай активность, силовые упражнения и сбалансированное питание. Избегай жёстких ограничений и высыпайся.",
            CoachTopic.MUSCLE_GAIN to "План набора мышц, {NAME}: постепенно добавляй повторения или вес, соблюдай технику и оставляй время на восстановление.",
            CoachTopic.CHEST_PUSH to "Для груди, {NAME}: зафиксируй лопатки, контролируй опускание и выбирай вес, с которым можешь сохранять правильную технику.",
            CoachTopic.ARMS_SHOULDERS to "Для рук и плеч, {NAME}: двигай вес подконтрольно, держи локти стабильно и не раскачивай корпус.",
            CoachTopic.BACK_POSTURE to "Для сильной спины, {NAME}: выполняй тяги локтями, держи корпус устойчивым и делай активные перерывы.",
            CoachTopic.LEGS_GLUTES to "Для ног, {NAME}: держи колени на линии стоп, контролируй повторения и ставь технику выше веса.",
            CoachTopic.ABS_CORE to "Для мышц корпуса, {NAME}: напряги живот, сохраняй нейтральное положение позвоночника и ровно дыши. Контроль важнее скорости.",
            CoachTopic.CARDIO_HIIT to "Для выносливости, {NAME}: начни с темпа, при котором можешь говорить. Постепенно увеличивай время или интенсивность и восстанавливайся.",
            CoachTopic.YOUTH_GROWTH to "Для роста и осанки, {NAME}: важны сон, разнообразное питание, ежедневная активность и тренировки по возрасту. Упражнения не гарантируют увеличение роста.",
            CoachTopic.RECOVERY_SLEEP to "Для восстановления, {NAME}: придерживайся режима сна, сократи яркий экран перед сном и планируй отдых после интенсивных тренировок.",
            CoachTopic.HYDRATION_SUPPS to "Для питьевого режима, {NAME}: регулярно пей воду с учётом жажды и активности. Добавки не заменяют питание; проверяй состав и дозировки.",
            CoachTopic.MOTIVATION_PLATEAU to "Если прогресс остановился, {NAME}: оценивай изменения за несколько недель. Меняй один параметр за раз и дай плану время.",
            CoachTopic.GENERAL_PLAN to "Хорошее начало, {NAME}: запланируй 2–4 тренировки в неделю, сохраняй лёгкую активность в дни отдыха и записывай результаты."
        ),
        "nl" to mapOf(
            CoachTopic.GREETING to "Hoi {NAME}! Ik ben FitChicken. Stel vragen over training, voeding, houding of herstel.",
            CoachTopic.PROTEIN_NUTRITION to "Voor {NAME}: verdeel ongeveer {PROTEIN_G} g eiwit over 3–4 maaltijden. Kies eieren, yoghurt, peulvruchten, vis en mager vlees.",
            CoachTopic.FAT_LOSS to "Voor duurzame vooruitgang, {NAME}: combineer regelmatige beweging, krachttraining en evenwichtige maaltijden. Vermijd extreme beperkingen en slaap voldoende.",
            CoachTopic.MUSCLE_GAIN to "Je spieropbouwplan, {NAME}: verhoog herhalingen of gewicht geleidelijk, houd je techniek goed en plan voldoende herstel.",
            CoachTopic.CHEST_PUSH to "Bij borstoefeningen, {NAME}: houd je schouderbladen stabiel, laat het gewicht gecontroleerd zakken en kies een beheersbare belasting.",
            CoachTopic.ARMS_SHOULDERS to "Voor armen en schouders, {NAME}: beweeg gecontroleerd, houd je ellebogen stabiel en gebruik geen zwaai vanuit je rug.",
            CoachTopic.BACK_POSTURE to "Voor een sterke rug, {NAME}: leid roeibewegingen met je ellebogen, houd je romp stabiel en neem actieve beweegpauzes.",
            CoachTopic.LEGS_GLUTES to "Bij beentraining, {NAME}: houd je knieën in lijn met je voeten, controleer elke herhaling en zet techniek vóór gewicht.",
            CoachTopic.ABS_CORE to "Voor je core, {NAME}: span je buik aan, houd je wervelkolom neutraal en adem rustig. Controle is belangrijker dan snelheid.",
            CoachTopic.CARDIO_HIIT to "Voor conditie, {NAME}: begin op een tempo waarbij je nog kunt praten. Verhoog duur of intensiteit geleidelijk en plan herstel.",
            CoachTopic.YOUTH_GROWTH to "Voor groei en houding, {NAME}: slaap, gevarieerd eten, dagelijkse beweging en leeftijdsgeschikte training zijn belangrijk. Oefeningen garanderen geen extra lengte.",
            CoachTopic.RECOVERY_SLEEP to "Voor beter herstel, {NAME}: houd vaste slaaptijden aan, beperk fel schermlicht voor het slapengaan en neem rust na intensieve trainingen.",
            CoachTopic.HYDRATION_SUPPS to "Voor hydratatie, {NAME}: drink regelmatig op basis van dorst en activiteit. Supplementen vervangen geen evenwichtige voeding; controleer de dosering.",
            CoachTopic.MOTIVATION_PLATEAU to "Loop je vast, {NAME}: bekijk je vooruitgang over meerdere weken. Verander één onderdeel tegelijk en geef het plan tijd.",
            CoachTopic.GENERAL_PLAN to "Goede start, {NAME}: plan 2–4 passende trainingen per week, blijf licht bewegen op rustdagen en noteer je voortgang."
        ),
        "pl" to mapOf(
            CoachTopic.GREETING to "Cześć {NAME}! Tu FitChicken. Pytaj o trening, odżywianie, postawę lub regenerację.",
            CoachTopic.PROTEIN_NUTRITION to "Dla Ciebie, {NAME}: rozłóż około {PROTEIN_G} g białka na 3–4 posiłki. Wybieraj jajka, jogurt, rośliny strączkowe, ryby i chude mięso.",
            CoachTopic.FAT_LOSS to "Aby robić trwałe postępy, {NAME}: łącz ruch, trening siłowy i zbilansowane posiłki. Unikaj skrajnych ograniczeń i zadbaj o sen.",
            CoachTopic.MUSCLE_GAIN to "Plan budowania mięśni, {NAME}: stopniowo dodawaj powtórzenia lub obciążenie, pilnuj techniki i zapewnij sobie regenerację.",
            CoachTopic.CHEST_PUSH to "Podczas ćwiczeń klatki, {NAME}: ustabilizuj łopatki, kontroluj opuszczanie i dobierz ciężar pozwalający zachować technikę.",
            CoachTopic.ARMS_SHOULDERS to "Dla ramion i barków, {NAME}: wykonuj ruch kontrolowanie, utrzymuj stabilne łokcie i nie pomagaj sobie zamachem tułowia.",
            CoachTopic.BACK_POSTURE to "Aby wzmocnić plecy, {NAME}: prowadź ruch łokciami, stabilizuj tułów i rób aktywne przerwy.",
            CoachTopic.LEGS_GLUTES to "Podczas treningu nóg, {NAME}: ustaw kolana w linii ze stopami, kontroluj powtórzenia i stawiaj technikę ponad ciężarem.",
            CoachTopic.ABS_CORE to "Dla mięśni brzucha, {NAME}: napnij brzuch, utrzymuj neutralne ustawienie kręgosłupa i oddychaj spokojnie. Kontrola jest ważniejsza niż szybkość.",
            CoachTopic.CARDIO_HIIT to "Dla kondycji, {NAME}: zacznij w tempie, które pozwala Ci jeszcze rozmawiać. Stopniowo zwiększaj czas lub intensywność i pamiętaj o regeneracji.",
            CoachTopic.YOUTH_GROWTH to "Dla wzrostu i postawy, {NAME}: ważne są sen, różnorodne jedzenie, codzienny ruch i trening dostosowany do wieku. Ćwiczenia nie gwarantują dodatkowego wzrostu.",
            CoachTopic.RECOVERY_SLEEP to "Aby lepiej się regenerować, {NAME}: zachowuj regularne pory snu, ogranicz jasne ekrany przed zaśnięciem i odpoczywaj po intensywnych treningach.",
            CoachTopic.HYDRATION_SUPPS to "W kwestii nawodnienia, {NAME}: pij regularnie, uwzględniając pragnienie i aktywność. Suplementy nie zastępują diety; sprawdzaj skład i dawki.",
            CoachTopic.MOTIVATION_PLATEAU to "Gdy pojawia się zastój, {NAME}: oceniaj postępy w skali kilku tygodni. Zmieniaj jedną rzecz naraz i daj planowi czas.",
            CoachTopic.GENERAL_PLAN to "Dobry początek, {NAME}: zaplanuj 2–4 odpowiednie treningi tygodniowo, ruszaj się lekko w dni odpoczynku i zapisuj postępy."
        ),
        "pt" to mapOf(
            CoachTopic.GREETING to "Olá, {NAME}! Sou o FitChicken. Pergunta-me sobre treino, alimentação, postura ou recuperação.",
            CoachTopic.PROTEIN_NUTRITION to "Para {NAME}: distribui cerca de {PROTEIN_G} g de proteína por 3–4 refeições. Escolhe ovos, iogurte, leguminosas, peixe e carnes magras.",
            CoachTopic.FAT_LOSS to "Para progredires de forma sustentável, {NAME}: combina atividade regular, força e refeições equilibradas. Evita restrições extremas e dorme bem.",
            CoachTopic.MUSCLE_GAIN to "O teu plano muscular, {NAME}: aumenta gradualmente as repetições ou a carga, mantém uma técnica correta e reserva tempo para recuperar.",
            CoachTopic.CHEST_PUSH to "Nos exercícios de peito, {NAME}: estabiliza as omoplatas, controla a descida e escolhe uma carga que consigas controlar.",
            CoachTopic.ARMS_SHOULDERS to "Para braços e ombros, {NAME}: controla o movimento, mantém os cotovelos estáveis e evita balanço das costas.",
            CoachTopic.BACK_POSTURE to "Para fortalecer as costas, {NAME}: conduz as remadas com os cotovelos, mantém o tronco estável e faz pausas ativas.",
            CoachTopic.LEGS_GLUTES to "No treino de pernas, {NAME}: mantém os joelhos alinhados com os pés, controla cada repetição e privilegia a técnica.",
            CoachTopic.ABS_CORE to "Para o core, {NAME}: contrai o abdómen, mantém a coluna neutra e respira de forma regular. O controlo vale mais do que a velocidade.",
            CoachTopic.CARDIO_HIIT to "Para melhorar a resistência, {NAME}: começa num ritmo que ainda te permita falar. Aumenta o tempo ou a intensidade gradualmente e recupera bem.",
            CoachTopic.YOUTH_GROWTH to "Para crescimento e postura, {NAME}: sono, alimentação variada, movimento diário e treino adequado à idade são essenciais. O exercício não garante maior altura.",
            CoachTopic.RECOVERY_SLEEP to "Para recuperares melhor, {NAME}: mantém horários de sono regulares, reduz os ecrãs antes de dormir e descansa após sessões intensas.",
            CoachTopic.HYDRATION_SUPPS to "Para hidratação, {NAME}: bebe água regularmente conforme a sede e a atividade. Suplementos não substituem uma alimentação equilibrada; verifica as doses.",
            CoachTopic.MOTIVATION_PLATEAU to "Se ficares sem progresso, {NAME}: avalia a evolução ao longo de várias semanas. Altera apenas uma variável de cada vez e dá tempo ao plano.",
            CoachTopic.GENERAL_PLAN to "Bom começo, {NAME}: planeia 2–4 sessões adequadas por semana, mantém movimento leve nos dias de descanso e regista a evolução."
        )
    )

    fun topicReply(languageCode: String, topic: CoachTopic): String? =
        topics[languageCode.lowercase(java.util.Locale.ROOT)]?.get(topic)

    fun genericReply(languageCode: String): String = when (languageCode.lowercase(java.util.Locale.ROOT)) {
        "de" -> "Gute Frage, {NAME}! Für dein Ziel {GOAL} sind ein passender Trainingsrhythmus, ausgewogene Mahlzeiten und Erholung wichtig. Dein Tagesziel liegt bei {KCAL} kcal und etwa {PROTEIN_G} g Protein."
        "fr" -> "Bonne question, {NAME} ! Pour ton objectif {GOAL}, privilégie un entraînement adapté, des repas équilibrés et une bonne récupération. Ton repère quotidien est de {KCAL} kcal et environ {PROTEIN_G} g de protéines."
        "es" -> "¡Buena pregunta, {NAME}! Para tu objetivo de {GOAL}, prioriza un entrenamiento adecuado, comidas equilibradas y buena recuperación. Tu referencia diaria es {KCAL} kcal y unos {PROTEIN_G} g de proteína."
        "it" -> "Ottima domanda, {NAME}! Per il tuo obiettivo {GOAL}, punta su allenamento adatto, pasti equilibrati e buon recupero. Il riferimento giornaliero è {KCAL} kcal e circa {PROTEIN_G} g di proteine."
        "ru" -> "Хороший вопрос, {NAME}! Для цели «{GOAL}» важны подходящие тренировки, сбалансированное питание и восстановление. Твой ориентир на день — {KCAL} ккал и около {PROTEIN_G} г белка."
        "nl" -> "Goede vraag, {NAME}! Voor je doel {GOAL} zijn passende trainingen, evenwichtige maaltijden en herstel belangrijk. Je richtlijn is {KCAL} kcal en ongeveer {PROTEIN_G} g eiwit per dag."
        "pl" -> "Dobre pytanie, {NAME}! Przy celu {GOAL} ważne są odpowiednie treningi, zbilansowane posiłki i regeneracja. Dzienna wartość orientacyjna to {KCAL} kcal i około {PROTEIN_G} g białka."
        "pt" -> "Boa pergunta, {NAME}! Para o teu objetivo {GOAL}, aposta num treino adequado, refeições equilibradas e boa recuperação. A referência diária é {KCAL} kcal e cerca de {PROTEIN_G} g de proteína."
        else -> "Great question, {NAME}! Keep a consistent training rhythm, balanced meals and enough recovery. Your daily guide is {KCAL} kcal and about {PROTEIN_G} g of protein."
    }

    fun youthSafety(languageCode: String): String = when (languageCode.lowercase(java.util.Locale.ROOT)) {
        "de" -> "\n\n🛡️ Hinweis für Jugendliche: Technik, altersgerechtes Training, Schlaf und Erholung haben Vorrang vor maximalen Gewichten."
        "fr" -> "\n\n🛡️ Conseil pour les jeunes : privilégie la technique, un entraînement adapté à l'âge, le sommeil et la récupération."
        "es" -> "\n\n🛡️ Nota para jóvenes: prioriza la técnica, el entrenamiento adecuado a tu edad, el sueño y la recuperación."
        "it" -> "\n\n🛡️ Nota per i giovani: dai priorità alla tecnica, a un allenamento adatto all'età, al sonno e al recupero."
        "ru" -> "\n\n🛡️ Для подростков: техника, тренировки по возрасту, сон и восстановление важнее максимальных весов."
        "nl" -> "\n\n🛡️ Voor jongeren: geef voorrang aan techniek, leeftijdsgeschikte training, slaap en herstel."
        "pl" -> "\n\n🛡️ Wskazówka dla młodych: technika, trening dostosowany do wieku, sen i regeneracja są ważniejsze niż maksymalne ciężary."
        "pt" -> "\n\n🛡️ Nota para jovens: dá prioridade à técnica, ao treino adequado à idade, ao sono e à recuperação."
        else -> "\n\n🛡️ Youth note: prioritize good technique, age-appropriate training, sleep and recovery over maximum weights."
    }

    fun champion(languageCode: String): String = when (languageCode.lowercase(java.util.Locale.ROOT)) {
        "de", "fr" -> "Champion"
        "es" -> "Campeón"
        "it" -> "Campione"
        "ru" -> "Чемпион"
        "nl" -> "Kampioen"
        "pl" -> "Mistrzu"
        "pt" -> "Campeão"
        "tr" -> "Şampiyon"
        else -> "Champ"
    }

    fun goal(languageCode: String, goal: String): String {
        val index = when (goal) {
            "fat_loss" -> 0
            "muscle" -> 1
            "growth_posture" -> 2
            else -> 3
        }
        val labels = mapOf(
            "de" to listOf("Fettabbau", "Muskeln und Kraft", "Haltung und sportliche Entwicklung", "Allgemeine Fitness"),
            "fr" to listOf("perte de graisse", "muscle et force", "posture et développement sportif", "condition physique générale"),
            "es" to listOf("pérdida de grasa", "músculo y fuerza", "postura y desarrollo atlético", "acondicionamiento general"),
            "it" to listOf("perdita di grasso", "muscoli e forza", "postura e sviluppo atletico", "condizionamento generale"),
            "ru" to listOf("снижение жира", "мышцы и сила", "осанка и физическое развитие", "общая физическая форма"),
            "nl" to listOf("vetverlies", "spieren en kracht", "houding en atletische ontwikkeling", "algemene conditie"),
            "pl" to listOf("redukcja tkanki tłuszczowej", "mięśnie i siła", "postawa i rozwój sportowy", "ogólna sprawność"),
            "pt" to listOf("perda de gordura", "massa muscular e força", "postura e desenvolvimento atlético", "condição física geral"),
            "tr" to listOf("yağ yakımı", "kas ve güç", "postür ve atletik gelişim", "genel kondisyon"),
            "en" to listOf("fat loss", "muscle and strength", "posture and athletic growth", "general conditioning")
        )
        return labels[languageCode.lowercase(java.util.Locale.ROOT)]?.get(index) ?: labels.getValue("en")[index]
    }
}
