package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.analytics.speed.GetTypingSpeedUseCase
import com.dapsoft.wpmcounter.common.validation.WordValidator
import com.dapsoft.wpmcounter.analytics.speed.TypingSpeedState
import com.dapsoft.wpmcounter.logger.Logger

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.seconds

internal class GetTypingSpeedUseCaseImpl(
    private val analyticsRepo: BehavioralAnalyticsRepository,
    private val speedCalculator: SpeedCalculator,
    private val sessionUpdater: TypingSessionUpdater,
    private val log: Logger
) : GetTypingSpeedUseCase {

    override fun invoke(validator: WordValidator): Flow<TypingSpeedState> =
        flow {
            runCatching { analyticsRepo.getLatestEvent() }
                .onSuccess { source ->
                    emitAll(
                        source.filterNotNull()
                            .map { event ->
                                val sessionState = sessionUpdater.onEvent(event.symbol, event.eventTime, PAUSE_THRESHOLD, validator)
                                log.d(TAG, "Current session state: $sessionState")
                                TypingSpeedState.Active(
                                    wordsPerMinute = speedCalculator.calculateWordsPerMinute(sessionState.validWordCount, sessionState.totalActiveTypingTime),
                                )
                            }
                            .flatMapLatest { activeState ->
                                flow {
                                    emit(activeState)
                                    delay(PAUSE_THRESHOLD)
                                    emit(TypingSpeedState.Paused)
                                }

                            }
                            .distinctUntilChanged()
                            .onEach {
                                log.d(TAG, "Emitting typing speed state: $it")
                            }
                    )
                }
                .onFailure { exception ->
                    if (exception is CancellationException) {
                        throw exception
                    }
                    log.e(TAG, "Failed to obtain latest events flow: ${exception.stackTraceToString()}")
                    emit(TypingSpeedState.Error)
                }
    }

    companion object {
        private val PAUSE_THRESHOLD = 5.seconds
        private val TAG = GetTypingSpeedUseCaseImpl::class.java.simpleName
    }
}
