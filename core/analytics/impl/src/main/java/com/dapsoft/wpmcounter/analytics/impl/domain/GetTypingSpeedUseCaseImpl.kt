package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.analytics.GetTypingSpeedUseCase
import com.dapsoft.wpmcounter.analytics.WordValidator
import com.dapsoft.wpmcounter.analytics.impl.domain.model.KeyEvent
import com.dapsoft.wpmcounter.logger.Logger

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

internal class GetTypingSpeedUseCaseImpl(
    private val analyticsRepo: BehavioralAnalyticsRepository,
    private val typingSpeedRepository: TypingSpeedRepository,
    private val log: Logger
) : GetTypingSpeedUseCase {

    override fun invoke(validator: WordValidator): Flow<Float> {
        return analyticsRepo.getLatestEvent().map { event ->
            event?.let {
                calculateWordsPerMinute(event, validator)
            } ?: calculateSpeed()
        }.distinctUntilChanged()
    }

    private fun calculateWordsPerMinute(event: KeyEvent, validator: WordValidator): Float {
        if (typingSpeedRepository.startTimestamp == 0L) {
            typingSpeedRepository.startTimestamp = event.keyPressTime
            typingSpeedRepository.lastTimestamp = event.keyReleaseTime
            return 0f
        }

        val timeDiff = event.keyPressTime - typingSpeedRepository.lastTimestamp
        val isWithinPauseThreshold = timeDiff < PAUSE_THRESHOLD.inWholeMilliseconds

        if (isWithinPauseThreshold) {
            typingSpeedRepository.addTimeToCurrentWord(timeDiff)
        }

        val char = event.keyCode

        if (char == ' ' || char == '\n' || char == '\t') {
            val currentWord = typingSpeedRepository.getCurrentWord()
            if (currentWord.isNotEmpty()) {
                val word = currentWord
                if (validator.isValidWord(word)) {
                    typingSpeedRepository.validWordCount = typingSpeedRepository.validWordCount + 1
                    typingSpeedRepository.totalActiveTypingTimeMillis += typingSpeedRepository.getCurrentWordTypingTime()
                }
                typingSpeedRepository.clearCurrentWord()
            }
        } else {
            typingSpeedRepository.appendSymbolToCurrentWord(char)
        }

        typingSpeedRepository.lastTimestamp = event.keyReleaseTime
        return calculateSpeed()
    }

    private fun calculateSpeed(): Float {
        val activeTimeMinutes = maxOf(
            typingSpeedRepository.totalActiveTypingTimeMillis / 1.minutes.inWholeMilliseconds.toFloat(),
            0.01F
        )
        val result = typingSpeedRepository.validWordCount.toFloat() / activeTimeMinutes
        log.d(TAG, "Calculating typing speed: validWordCount=${typingSpeedRepository.validWordCount}, " +
                "totalActiveTypingTimeMillis=${typingSpeedRepository.totalActiveTypingTimeMillis}" +
        ", activeTimeMinutes=$activeTimeMinutes, result=$result")
        return result
    }

    companion object {
        private val PAUSE_THRESHOLD = 5.seconds
        private val TAG = GetTypingSpeedUseCaseImpl::class.java.name
    }
}