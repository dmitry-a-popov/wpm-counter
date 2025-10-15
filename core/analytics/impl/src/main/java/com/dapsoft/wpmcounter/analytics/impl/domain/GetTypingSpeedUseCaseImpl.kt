package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.analytics.speed.GetTypingSpeedUseCase
import com.dapsoft.wpmcounter.common.validation.WordValidator
import com.dapsoft.wpmcounter.analytics.speed.TypingSpeedState
import com.dapsoft.wpmcounter.logger.Logger

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlin.time.Duration.Companion.seconds

internal class GetTypingSpeedUseCaseImpl(
    private val analyticsRepo: BehavioralAnalyticsRepository,
    private val speedCalculator: SpeedCalculator,
    private val sessionUpdater: TypingSessionUpdater,
    private val log: Logger
) : GetTypingSpeedUseCase {

    override fun invoke(validator: WordValidator): Flow<TypingSpeedState> {
        return analyticsRepo
            .getLatestEvent()
            .filterNotNull()
            .map { event ->
                val sessionState = sessionUpdater.onEvent(event.symbol, event.eventTimeMillis, PAUSE_THRESHOLD, validator)
                log.d(TAG, "Current session state: $sessionState")
                TypingSpeedState(
                    wordsPerMinute = speedCalculator.calculateWordPerMinute(sessionState.validWordCount, sessionState.totalActiveTypingTimeMillis),
                    isActive = true
                )
            }
            .flatMapLatest { activeState ->
                flow {
                    emit(activeState)
                    delay(PAUSE_THRESHOLD.inWholeMilliseconds)
                    emit(activeState.copy(isActive = false))
                }

            }
            .distinctUntilChanged()
            .onEach {
                log.d(TAG, "Emitting typing speed state: $it")
            }
    }

    companion object {
        private val PAUSE_THRESHOLD = 5.seconds
        private val TAG = GetTypingSpeedUseCaseImpl::class.java.simpleName
    }
}