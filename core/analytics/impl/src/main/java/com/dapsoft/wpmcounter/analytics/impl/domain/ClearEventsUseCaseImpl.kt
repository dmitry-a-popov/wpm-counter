package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.analytics.ClearEventsUseCase
import com.dapsoft.wpmcounter.logger.Logger

import javax.inject.Inject

import kotlin.coroutines.cancellation.CancellationException

/**
 * Use case that clears all persisted behavioral analytics events and,
 * if successful, resets the in-memory typing session state.
 *
 * Failure policy: session state is not reset if event deletion fails,
 * preventing inconsistent "mixed" state.
 */
internal class ClearEventsUseCaseImpl @Inject constructor(
    private val behavioralAnalyticsRepository: BehavioralAnalyticsRepository,
    private val typingSessionStateStore: TypingSessionStateStore,
    private val log: Logger
) : ClearEventsUseCase {

    override suspend fun invoke() = runCatching {
        behavioralAnalyticsRepository.deleteAllEvents()
        typingSessionStateStore.reset()
    }.onFailure { exception ->
        log.e(TAG, exception.stackTraceToString())
        if (exception is CancellationException) {
            throw exception
        }
    }

    private companion object {
        private val TAG = ClearEventsUseCaseImpl::class.java.simpleName
    }
}
