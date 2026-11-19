package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import java.util.concurrent.Executors
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Lightweight, crash-safe procedural UI & workout sound-effects synthesizer.
 * Generates soft harmonic chime cues off the main thread without external audio assets.
 * Skips hardware playback harmlessly under Robolectric or devices without audio output.
 */
object SoundEffectsManager {
    private const val SAMPLE_RATE = 22050

    @Volatile
    private var enabled: Boolean = true

    private val executor = Executors.newSingleThreadExecutor { runnable ->
        Thread(runnable, "FitChicken-SFX").apply { isDaemon = true }
    }

    fun isEnabled(): Boolean = enabled

    fun setEnabled(value: Boolean) {
        enabled = value
    }

    fun playTap() {
        playNotes(
            notes = listOf(ToneSpec(freqHz = 660.0, durationMs = 45, volume = 0.14))
        )
    }

    fun playWorkoutStart() {
        playNotes(
            notes = listOf(
                ToneSpec(freqHz = 523.25, durationMs = 75, volume = 0.20),
                ToneSpec(freqHz = 783.99, durationMs = 115, volume = 0.22)
            )
        )
    }

    fun playSetComplete() {
        playNotes(
            notes = listOf(
                ToneSpec(freqHz = 587.33, durationMs = 60, volume = 0.19),
                ToneSpec(freqHz = 880.00, durationMs = 95, volume = 0.21)
            )
        )
    }

    fun playWorkoutComplete() {
        playNotes(
            notes = listOf(
                ToneSpec(freqHz = 523.25, durationMs = 80, volume = 0.20),
                ToneSpec(freqHz = 659.25, durationMs = 80, volume = 0.21),
                ToneSpec(freqHz = 783.99, durationMs = 90, volume = 0.22),
                ToneSpec(freqHz = 1046.50, durationMs = 160, volume = 0.24)
            )
        )
    }

    fun playXpEarned() {
        playNotes(
            notes = listOf(
                ToneSpec(freqHz = 659.25, durationMs = 65, volume = 0.18),
                ToneSpec(freqHz = 987.77, durationMs = 115, volume = 0.20)
            )
        )
    }

    fun playProUnlocked() {
        playNotes(
            notes = listOf(
                ToneSpec(freqHz = 523.25, durationMs = 70, volume = 0.19),
                ToneSpec(freqHz = 659.25, durationMs = 70, volume = 0.20),
                ToneSpec(freqHz = 783.99, durationMs = 80, volume = 0.22),
                ToneSpec(freqHz = 1046.50, durationMs = 140, volume = 0.23),
                ToneSpec(freqHz = 1318.51, durationMs = 180, volume = 0.22)
            )
        )
    }

    private data class ToneSpec(
        val freqHz: Double,
        val durationMs: Int,
        val volume: Double
    )

    private fun isEnvironmentSafeForAudio(): Boolean {
        val fp = Build.FINGERPRINT ?: ""
        return !fp.equals("robolectric", ignoreCase = true)
    }

    private fun playNotes(notes: List<ToneSpec>) {
        if (!enabled || !isEnvironmentSafeForAudio() || notes.isEmpty()) return
        runCatching {
            executor.execute {
                if (!enabled) return@execute
                playSequenceSync(notes)
            }
        }
    }

    private fun playSequenceSync(notes: List<ToneSpec>) {
        val pcm = synthesizePcm16(notes)
        if (pcm.isEmpty()) return
        val byteCount = pcm.size * 2
        var track: AudioTrack? = null
        try {
            track = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(SAMPLE_RATE)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(byteCount)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            if (track.state != AudioTrack.STATE_INITIALIZED) {
                return
            }
            val written = track.write(pcm, 0, pcm.size)
            if (written <= 0) {
                return
            }
            track.play()
            val totalDurationMs = notes.sumOf { it.durationMs }.toLong() + 35L
            Thread.sleep(totalDurationMs)
        } catch (_: Throwable) {
            // Gracefully ignore audio hardware/focus unavailability
        } finally {
            runCatching {
                if (track?.playState == AudioTrack.PLAYSTATE_PLAYING) {
                    track.stop()
                }
            }
            runCatching { track?.release() }
        }
    }

    internal fun synthesizePcm16(
        freqsHz: FloatArray,
        toneMs: Int,
        peakGain: Float
    ): ShortArray = synthesizePcm16(
        freqsHz.map { ToneSpec(freqHz = it.toDouble(), durationMs = toneMs, volume = peakGain.toDouble()) }
    )

    private fun synthesizePcm16(notes: List<ToneSpec>): ShortArray {
        val gapSamples = (SAMPLE_RATE * 10) / 1000 // 10ms gentle separation
        val noteSampleCounts = notes.map { ((SAMPLE_RATE * it.durationMs) / 1000).coerceAtLeast(1) }
        val totalSamples = noteSampleCounts.sum() + gapSamples * (notes.size - 1).coerceAtLeast(0)
        val buffer = ShortArray(totalSamples)
        var offset = 0
        notes.forEachIndexed { idx, spec ->
            val count = noteSampleCounts[idx]
            val fadeSamples = (count / 5).coerceIn(1, 220)
            for (i in 0 until count) {
                val t = i.toDouble() / SAMPLE_RATE
                val attack = if (i < fadeSamples) {
                    0.5 * (1.0 - cos(PI * i / fadeSamples))
                } else 1.0
                val release = if (i > count - fadeSamples) {
                    0.5 * (1.0 - cos(PI * (count - i) / fadeSamples))
                } else 1.0
                // Fundamental + subtle second harmonic for warm chime texture
                val wave = 0.85 * sin(2.0 * PI * spec.freqHz * t) +
                    0.15 * sin(2.0 * PI * (spec.freqHz * 2.0) * t)
                val sample = (wave * spec.volume * attack * release * Short.MAX_VALUE)
                    .toInt()
                    .coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
                    .toShort()
                buffer[offset + i] = sample
            }
            offset += count
            if (idx < notes.lastIndex) {
                offset += gapSamples
            }
        }
        return buffer
    }
}
