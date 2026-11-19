# FitChicken / Koç Tavuk – Language & Animation Patch

This patch is based on the first Android source ZIP supplied in the conversation.

## Fixed

- Device-language detection now recognizes all 10 supported locales: English, Turkish, German, French, Spanish, Italian, Russian, Dutch, Polish and Portuguese. Unsupported device locales fall back to English.
- Added/filled the missing English badge resource entries so the translated resource sets stay aligned.
- The FitChicken Coach home card keeps the mascot and title together and places the advice in a full-width, wrapping text area. Long advice is no longer forced into the narrow space beside the mascot.
- Offline Coach replies now have localized copy for all 15 topics in the eight non-English/non-Turkish languages; common topic keywords were expanded for those locales.
- Daily nutrition feedback now has localized text for all five feedback states in all 10 languages.
- Badge titles/descriptions use localized string resources. Secondary-muscle labels have translations for common muscle groups in the other eight locales.
- Body-fat percentages use locale-aware percent formatting.
- Mascot expression frames crossfade smoothly; the existing breathing and interaction movement are retained.
- Exercise start/peak cards use verified exercise-specific media mappings, show the full image (`Fit` rather than crop), softly highlight the active position and use a neutral, localized “Technique demo” label rather than implying a gender-specific photo.
- Corrected the free-exercise-db image URL shape across exercise details, program cover images and custom-workout cover images: `<exercise-folder>/0.jpg` and `<exercise-folder>/1.jpg` (the old `/images/` path was incorrect).
- Removed the old broad muscle-group image substitution logic from the exercise detail player. Eight known non-exact exercise mappings now show a neutral placeholder rather than a misleading image.
- Added regression tests for locale-aware Coach/nutrition text, percentage formatting, exercise image URL paths and separate curl-image mappings.

## Important coverage limits

- The app's food catalog model stores only English and Turkish food names and portion hints. Therefore, food names/portion hints still fall back to English in the other eight languages.
- The separate A–Z exercise database also stores its long exercise names/instructions in English and Turkish only. The main workout catalog has its translated resource strings, but those A–Z entries still fall back to English outside Turkish/English.
- Exercise photos are fetched from the public exercise dataset, so first-time loading needs an internet connection. When the exact photo is unavailable or the device is offline, the exercise screen now shows a placeholder instead of a different exercise photo.

## Validation performed

- All XML resource files parsed successfully.
- The 10 locale resource sets were checked for key parity (1,652 common resource entries per locale; the two Firebase configuration tokens are intentionally supplied only by the default resource set).
- All 15 offline Coach topics are present in each of the eight added Coach locales; all five nutrition scenarios are present in each of the 10 locales.
- Changed Kotlin files were checked for parser-level syntax errors, and regression tests were added/updated.
- A full Gradle/Android build and device test were not run in this environment because the supplied project has no `gradle-wrapper.jar` and no installed Gradle/Android SDK. Build the ZIP in Google AI Studio or Android Studio before publishing.


## Rank journey and small-screen patch

- Added seven separate transparent PNG rank crests (Beginner, Fit Novice, Fitness Star, Rising Champion, Champion, Elite Athlete and Legend). The artwork contains no rank lettering, so translated names remain live text.
- Reworked XP rank thresholds to 0–499, 500–1,499, 1,500–2,999, 3,000–4,999, 5,000–7,499, 7,500–9,999 and 10,000+ XP. The 100-XP user level remains independent from rank tiers.
- Added a responsive rank-progress sheet with all seven badges, current/unlocked/locked states, workout count and 1/7/30/90-day streak milestones.
- The home rank title now wraps instead of being forced into one line; the daily check-in action uses its own full-width row; next-rank text wraps and the rank card opens the complete progression view.
- Added a next-day return prompt after the daily reward has been claimed.
- Search still uses a precomputed index, now normalized for diacritics, Turkish dotless-i and punctuation so terms such as “gogus” also find “göğüs”.
- Added rank threshold, translation and search-normalization regression assertions.
- Workout completion now updates the planner calendar immediately, records the completed session, awards XP and updates the workout count; XP, streak, quest and session totals are also included in the optional signed-in cloud profile sync and corresponding Firestore rules.


## Validation for this update

- All 101 Android XML resource files parsed successfully; the new rank and milestone strings are present in the 10 supported language resource sets.
- All seven rank PNGs were checked as separate 512×512 RGBA files with transparent backgrounds.
- `firestore.test.js` passed Node's syntax check; changed Kotlin files passed static delimiter checks.
- A full Android/Gradle build and emulator test were not run: this archive does not include `gradlew` or `gradle-wrapper.jar`, and the environment has no installed Gradle or Android SDK.
