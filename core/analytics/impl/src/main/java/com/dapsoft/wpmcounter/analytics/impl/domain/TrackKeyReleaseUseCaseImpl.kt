package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.analytics.TrackKeyReleaseUseCase
import com.dapsoft.wpmcounter.common.TimeProvider

internal class TrackKeyReleaseUseCaseImpl(
    private val pendingKeyEventsRepository: PendingKeyEventsRepository,
    private val behavioralAnalyticsRepository: BehavioralAnalyticsRepository,
    private val timeProvider: TimeProvider
) : TrackKeyReleaseUseCase {

    override suspend fun invoke(keyCode: Char, phoneOrientation: Int, username: String) {
        val currentTime = timeProvider.getElapsedRealtime()
        val pendingEvent = pendingKeyEventsRepository.getAndRemovePendingKeyEvent(keyCode)
            ?: throw IllegalStateException("No pending key event found for key code: $keyCode")

        val completeEvent = pendingEvent.copy(keyReleaseTime = currentTime)
        behavioralAnalyticsRepository.saveKeyEvent(completeEvent)
    }
}