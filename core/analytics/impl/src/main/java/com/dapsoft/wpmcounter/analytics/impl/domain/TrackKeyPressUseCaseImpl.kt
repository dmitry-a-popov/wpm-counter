package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.analytics.TrackKeyPressUseCase
import com.dapsoft.wpmcounter.analytics.impl.domain.model.KeystrokeEvent
import com.dapsoft.wpmcounter.common.TimeProvider
import com.dapsoft.wpmcounter.common.orientation.ScreenOrientationProvider
import com.dapsoft.wpmcounter.logger.Logger

import javax.inject.Inject

import kotlin.coroutines.cancellation.CancellationException

internal class TrackKeyPressUseCaseImpl @Inject constructor(
    private val behavioralAnalyticsRepository: BehavioralAnalyticsRepository,
    private val screenOrientationProvider: ScreenOrientationProvider,
    private val timeProvider: TimeProvider,
    private val log: Logger
) : TrackKeyPressUseCase {

    override suspend fun invoke(
        symbol: Char,
        userName: String
    ) = runCatching {
        require(!userName.isBlank())
        val keystrokeEvent = KeystrokeEvent(
            eventTime = timeProvider.getElapsedRealtime(),
            symbol = symbol,
            screenOrientation = screenOrientationProvider.currentOrientation,
            userName = userName
        )
        behavioralAnalyticsRepository.saveEvent(keystrokeEvent)
    }.onFailure { exception ->
        log.e(TAG, exception.stackTraceToString())
        if (exception is CancellationException) {
            throw exception
        }
    }

    private companion object {
        private val TAG = TrackKeyPressUseCaseImpl::class.java.simpleName
    }
}
