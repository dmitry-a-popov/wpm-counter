package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.analytics.TrackKeyPressUseCase
import com.dapsoft.wpmcounter.analytics.impl.domain.model.KeystrokeEvent
import com.dapsoft.wpmcounter.common.TimeProvider
import com.dapsoft.wpmcounter.common.orientation.ScreenOrientationProvider

internal class TrackKeyPressUseCaseImpl(
    private val behavioralAnalyticsRepository: BehavioralAnalyticsRepository,
    private val screenOrientationProvider: ScreenOrientationProvider,
    private val timeProvider: TimeProvider
) : TrackKeyPressUseCase {

    override suspend fun invoke(
        symbol: Char,
        username: String
    ) {
        val keystrokeEvent = KeystrokeEvent(
            eventTime = timeProvider.getElapsedRealtime(),
            symbol = symbol,
            screenOrientation = screenOrientationProvider.getCurrentOrientation(),
            username = username
        )
        behavioralAnalyticsRepository.saveEvent(keystrokeEvent)
    }
}
