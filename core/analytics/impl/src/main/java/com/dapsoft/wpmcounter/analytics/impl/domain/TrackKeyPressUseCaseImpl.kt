package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.analytics.TrackKeyPressUseCase
import com.dapsoft.wpmcounter.analytics.impl.domain.model.KeystrokeEvent
import com.dapsoft.wpmcounter.common.orientation.ScreenOrientation

import kotlin.time.Duration

internal class TrackKeyPressUseCaseImpl(
    private val behavioralAnalyticsRepository: BehavioralAnalyticsRepository
) : TrackKeyPressUseCase {

    override suspend fun invoke(
        symbol: Char,
        eventTime: Duration,
        phoneOrientation: ScreenOrientation,
        username: String
    ) {
        val keystrokeEvent = KeystrokeEvent(
            eventTime = eventTime,
            symbol = symbol,
            phoneOrientation = phoneOrientation,
            username = username
        )
        behavioralAnalyticsRepository.saveEvent(keystrokeEvent)
    }
}