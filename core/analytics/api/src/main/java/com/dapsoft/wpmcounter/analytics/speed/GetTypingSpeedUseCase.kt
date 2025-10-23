package com.dapsoft.wpmcounter.analytics.speed

import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

interface GetTypingSpeedUseCase {
    /**
     * Calculates typing speed based on validated input from database entries.
     * @param sampleText A reference text
     * @param pauseThreshold Duration to consider the typing paused. Defaults to 5 seconds. Value changes only in tests.
     * @return Flow of typing speed states
     */
    operator fun invoke(sampleText: String, pauseThreshold: Duration = 5.seconds): Flow<TypingSpeedState>
}
