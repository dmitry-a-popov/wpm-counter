package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.analytics.TrackKeyPressUseCase
import com.dapsoft.wpmcounter.analytics.impl.domain.model.KeyEvent
import com.dapsoft.wpmcounter.common.TimeProvider

internal class TrackKeyPressUseCaseImpl(
    private val pendingKeyEventsRepository: PendingKeyEventsRepository,
    private val timeProvider: TimeProvider
) : TrackKeyPressUseCase {

    override fun invoke(keyCode: Char, phoneOrientation: Int, username: String) {
        val currentTimeMillis = timeProvider.getElapsedRealtime()
        val pendingKeyEvent = KeyEvent(
            keyPressTime = currentTimeMillis,
            keyReleaseTime = 0,
            keyCode = keyCode,
            phoneOrientation = phoneOrientation,
            username = username
        )
        pendingKeyEventsRepository.savePendingKeyEvent(pendingKeyEvent)
    }
}