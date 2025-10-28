package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.analytics.speed.ObserveTypingSpeedUseCase
import com.dapsoft.wpmcounter.analytics.speed.TypingSpeedState
import com.dapsoft.wpmcounter.common.validation.TextValidator
import com.dapsoft.wpmcounter.logger.Logger
import com.dapsoft.wpmcounter.logger.d
import com.dapsoft.wpmcounter.logger.e

import javax.inject.Inject

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Emits `TypingSpeedState.Active` on each valid keystroke and switches to `TypingSpeedState.Paused`
 * if no events arrive within `pauseThreshold`. Emits `TypingSpeedState.Error` on non\-cancellation failures
 * but keeps the stream alive.
 */
@OptIn(ExperimentalTime::class)
internal class ObserveTypingSpeedUseCaseImpl @Inject constructor(
    private val analyticsRepo: BehavioralAnalyticsRepository,
    private val speedCalculator: SpeedCalculator,
    private val sessionUpdater: TypingSessionUpdater,
    private val textValidator: TextValidator,
    private val log: Logger
) : ObserveTypingSpeedUseCase {

    override fun invoke(
        sampleText: String,
        pauseThreshold: Duration
    ): Flow<TypingSpeedState> {
        require(pauseThreshold > Duration.ZERO)

        return analyticsRepo.observeLatestEvent()
            .filterNotNull()
            .map { event -> toActiveState(event.symbol, event.eventTime, pauseThreshold, sampleText) }
            .flatMapLatest { activeState ->
                flow {
                    emit(activeState)
                    delay(pauseThreshold)
                    emit(TypingSpeedState.Paused)
                }
            }
            .distinctUntilChanged()
            .onEach {
                log.d(TAG) { "Emitting typing speed state: $it" }
            }.catch { exception ->
                if (exception is CancellationException) {
                    throw exception
                }
                log.e(TAG, exception) { "Failed inside typing speed flow" }
                emit(TypingSpeedState.Error)
            }
    }

    private fun toActiveState(
        symbol: Char,
        eventTime: Instant,
        pauseThreshold: Duration,
        sampleText: String
    ): TypingSpeedState.Active {
        val sessionState = sessionUpdater.updateForKeystroke(symbol, eventTime, pauseThreshold)
        log.d(TAG) { "Session state: $sessionState" }
        val wpm = speedCalculator.calculateWordsPerMinute(
            textValidator.compareWords(sampleText, sessionState.currentText).count { it.matches },
            sessionState.totalActiveTypingTime
        )
        return TypingSpeedState.Active(wordsPerMinute = wpm)
    }

    companion object {
        private val TAG = ObserveTypingSpeedUseCaseImpl::class.java.simpleName
    }
}
