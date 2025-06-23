package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.analytics.TrackKeyReleaseUseCase
import com.dapsoft.wpmcounter.common.TimeProvider

internal class TrackKeyReleaseUseCaseImpl(
    private val pendingKeyEventsRepository: PendingKeyEventsRepository,
    private val behavioralAnalyticsRepository: BehavioralAnalyticsRepository
) : TrackKeyReleaseUseCase {

    override suspend fun invoke(keyCode: Int, eventTimeMillis: Long, phoneOrientation: Int, username: String) {
        val pendingEvent = pendingKeyEventsRepository.getAndRemovePendingKeyEvent(keyCode)
            ?: throw IllegalStateException("No pending key event found for key code: $keyCode")

        val completeEvent = pendingEvent.copy(keyReleaseTime = eventTimeMillis)
        behavioralAnalyticsRepository.saveKeyEvent(completeEvent)
    }
}