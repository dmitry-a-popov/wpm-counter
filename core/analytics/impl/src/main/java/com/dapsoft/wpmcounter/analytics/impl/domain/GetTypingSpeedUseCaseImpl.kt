package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.analytics.speed.GetTypingSpeedUseCase
import com.dapsoft.wpmcounter.common.validation.WordValidator
import com.dapsoft.wpmcounter.analytics.impl.domain.model.KeyEvent
import com.dapsoft.wpmcounter.analytics.speed.TypingSpeedState
import com.dapsoft.wpmcounter.logger.Logger

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.transformLatest
import kotlin.time.Duration.Companion.seconds

internal class GetTypingSpeedUseCaseImpl(
    private val analyticsRepo: BehavioralAnalyticsRepository,
    private val typingSpeedRepository: TypingSpeedRepository,
    private val wordBuffer: WordBuffer,
    private val speedCalculator: SpeedCalculator,
    private val log: Logger
) : GetTypingSpeedUseCase {

    override fun invoke(validator: WordValidator): Flow<TypingSpeedState> {
        return analyticsRepo.getLatestEvent().filterNotNull().transformLatest { event ->
            log.d(TAG, "Calculate speed for key event: $event")
            val speed = calculateWordsPerMinute(event, validator)
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
        return speedCalculator.calculateWordPerMinute(typingSpeedRepository.validWordCount, typingSpeedRepository.totalActiveTypingTimeMillis)
    }

    private fun processSymbol(symbol: Char, validator: WordValidator) {
        if (symbol.isWhitespace()) {
            val currentWord = wordBuffer.getCurrentWord()
            if (currentWord.isNotEmpty()) {
                if (validator.isValid(currentWord)) {
                    typingSpeedRepository.validWordCount += 1
                }
                wordBuffer.clearCurrentWord()
            }
        } else {
            wordBuffer.appendSymbolToCurrentWord(symbol)
        }
    }

    companion object {
        private val PAUSE_THRESHOLD = 5.seconds
        private val TAG = GetTypingSpeedUseCaseImpl::class.java.name
    }
}