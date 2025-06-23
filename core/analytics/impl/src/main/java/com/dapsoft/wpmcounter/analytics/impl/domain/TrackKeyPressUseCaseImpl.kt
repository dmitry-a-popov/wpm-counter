package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.analytics.TrackKeyPressUseCase
import com.dapsoft.wpmcounter.analytics.impl.domain.model.KeyEvent
import com.dapsoft.wpmcounter.common.TimeProvider

internal class TrackKeyPressUseCaseImpl(
    private val pendingKeyEventsRepository: PendingKeyEventsRepository
) : TrackKeyPressUseCase {

    override fun invoke(keyCode: Int, eventTimeMillis: Long, phoneOrientation: Int, username: String) {
        val pendingKeyEvent = KeyEvent(
            keyPressTime = eventTimeMillis,
            keyReleaseTime = 0,
            keyCode = keyCode,
            phoneOrientation = phoneOrientation,
            username = username
        )
        pendingKeyEventsRepository.savePendingKeyEvent(pendingKeyEvent)
    }
}