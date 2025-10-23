package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.analytics.speed.GetTypingSpeedUseCase
import com.dapsoft.wpmcounter.common.validation.WordValidator
import com.dapsoft.wpmcounter.analytics.speed.TypingSpeedState
import com.dapsoft.wpmcounter.logger.Logger

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

/**
 * Emits `TypingSpeedState.Active` on each valid keystroke and switches to `TypingSpeedState.Paused`
 * if no events arrive within `pauseThreshold`. Emits `TypingSpeedState.Error` on non\-cancellation failures
 * but keeps the stream alive.
 */
internal class GetTypingSpeedUseCaseImpl @Inject constructor(
    private val analyticsRepo: BehavioralAnalyticsRepository,
    private val speedCalculator: SpeedCalculator,
    private val sessionUpdater: TypingSessionUpdater,
    private val log: Logger
) : GetTypingSpeedUseCase {

    override fun invoke(
        validator: WordValidator,
        pauseThreshold: Duration
    ): Flow<TypingSpeedState> =
        analyticsRepo.getLatestEvent()
            .filterNotNull()
            .map { event -> toActiveState(event.symbol, event.eventTime, pauseThreshold, validator) }
            .flatMapLatest { activeState ->
                flow {
                    emit(activeState)
                    delay(pauseThreshold)
                    emit(TypingSpeedState.Paused)
                }
            }
            .distinctUntilChanged()
            .onEach {
                log.d(TAG, "Emitting typing speed state: $it")
            }.catch { exception ->
                if (exception is CancellationException) {
                    throw exception
                }
                log.e(TAG, "Failed inside typing speed flow: ${exception.stackTraceToString()}")
                emit(TypingSpeedState.Error)
            }

    private fun toActiveState(
        symbol: Char,
        eventTime: Duration,
        pauseThreshold: Duration,
        validator: WordValidator
    ): TypingSpeedState.Active {
        val sessionState = sessionUpdater.updateForKeystroke(symbol, eventTime, pauseThreshold, validator)
        log.d(TAG, "Session state: $sessionState")
        val wpm = speedCalculator.calculateWordsPerMinute(
            sessionState.validWordCount,
            sessionState.totalActiveTypingTime
        )
        return TypingSpeedState.Active(wordsPerMinute = wpm)
    }

    companion object {
        private val TAG = GetTypingSpeedUseCaseImpl::class.java.simpleName
    }
}
