package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.analytics.TrackKeyPressUseCase
import com.dapsoft.wpmcounter.analytics.impl.domain.model.KeyEvent
import com.dapsoft.wpmcounter.common.screenorientation.ScreenOrientation

internal class TrackKeyPressUseCaseImpl(
    private val behavioralAnalyticsRepository: BehavioralAnalyticsRepository
) : TrackKeyPressUseCase {

    override suspend fun invoke(
        keyCode: Char,
        eventTimeMillis: Long,
        phoneOrientation: ScreenOrientation,
        username: String
    ) {
        val keyEvent = KeyEvent(
            keyPressTime = eventTimeMillis,
            keyReleaseTime = eventTimeMillis,
            keyCode = keyCode,
            phoneOrientation = phoneOrientation,
            username = username
        )
        behavioralAnalyticsRepository.saveKeyEvent(keyEvent)
    }
}