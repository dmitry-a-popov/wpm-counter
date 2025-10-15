package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.analytics.TrackKeyPressUseCase
import com.dapsoft.wpmcounter.analytics.impl.domain.model.KeyEvent
import com.dapsoft.wpmcounter.common.orientation.ScreenOrientation

internal class TrackKeyPressUseCaseImpl(
    private val behavioralAnalyticsRepository: BehavioralAnalyticsRepository
) : TrackKeyPressUseCase {

    override suspend fun invoke(
        symbol: Char,
        eventTimeMillis: Long,
        phoneOrientation: ScreenOrientation,
        username: String
    ) {
        val keyEvent = KeyEvent(
            eventTimeMillis = eventTimeMillis,
            symbol = symbol,
            phoneOrientation = phoneOrientation,
            username = username
        )
        behavioralAnalyticsRepository.saveKeyEvent(keyEvent)
    }
}