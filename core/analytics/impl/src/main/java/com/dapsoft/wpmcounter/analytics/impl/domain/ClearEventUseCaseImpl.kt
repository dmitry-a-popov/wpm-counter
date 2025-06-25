package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.analytics.ClearEventsUseCase

internal class ClearEventUseCaseImpl(
    private val behavioralAnalyticsRepository: BehavioralAnalyticsRepository,
    private val typingSpeedRepository: TypingSpeedRepository
) : ClearEventsUseCase {

    override suspend fun invoke() {
        behavioralAnalyticsRepository.deleteAllEvents()
        typingSpeedRepository.clearState()
    }
}