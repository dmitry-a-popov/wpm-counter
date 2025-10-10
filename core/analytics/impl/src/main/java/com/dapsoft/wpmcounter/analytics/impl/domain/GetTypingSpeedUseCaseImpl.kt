package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.analytics.speed.GetTypingSpeedUseCase
import com.dapsoft.wpmcounter.common.validation.WordValidator
import com.dapsoft.wpmcounter.analytics.impl.domain.model.KeyEvent
import com.dapsoft.wpmcounter.analytics.speed.TypingSpeedState
import com.dapsoft.wpmcounter.logger.Logger

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.transformLatest
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

internal class GetTypingSpeedUseCaseImpl(
    private val analyticsRepo: BehavioralAnalyticsRepository,
    private val typingSpeedRepository: TypingSpeedRepository,
    private val wordRepository: WordRepository,
    private val log: Logger
) : GetTypingSpeedUseCase {

    override fun invoke(validator: WordValidator): Flow<TypingSpeedState> {
        return analyticsRepo.getLatestEvent().transformLatest { event ->
            val speed = event?.let {
                log.d(TAG, "Calculate speed for key event: $it")
                calculateWordsPerMinute(event, validator)
            } ?: calculateSpeed()
            emit(TypingSpeedState(wordsPerMinute = speed, isActive = true))
            delay(PAUSE_THRESHOLD)
            emit(TypingSpeedState(wordsPerMinute = speed, isActive = false))
        }.distinctUntilChanged()
    }

    private fun calculateWordsPerMinute(event: KeyEvent, validator: WordValidator): Float {
        if (typingSpeedRepository.startTimestamp == 0L) {
            typingSpeedRepository.startTimestamp = event.keyPressTime
            typingSpeedRepository.lastTimestamp = event.keyReleaseTime
            processSymbol(event.keyCode, validator)
            return 0f
        }

        val timeDiff = event.keyPressTime - typingSpeedRepository.lastTimestamp
        val isWithinPauseThreshold = timeDiff < PAUSE_THRESHOLD.inWholeMilliseconds

        if (isWithinPauseThreshold) {
            typingSpeedRepository.totalActiveTypingTimeMillis += timeDiff
        }

        processSymbol(event.keyCode, validator)

        typingSpeedRepository.lastTimestamp = event.keyReleaseTime
        return calculateSpeed()
    }

    private fun processSymbol(symbol: Char, validator: WordValidator) {
        if (symbol == ' ' || symbol == '\n' || symbol == '\t') {
            val currentWord = wordRepository.getCurrentWord()
            if (currentWord.isNotEmpty()) {
                if (validator.isValid(currentWord)) {
                    typingSpeedRepository.validWordCount += 1
                }
                wordRepository.clearCurrentWord()
            }
        } else {
            wordRepository.appendSymbolToCurrentWord(symbol)
        }
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