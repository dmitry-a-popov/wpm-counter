package com.dapsoft.wpmcounter.analytics.speed

import com.dapsoft.wpmcounter.common.validation.WordValidator

import kotlinx.coroutines.flow.Flow

interface GetTypingSpeedUseCase {
    /**
     * Calculates typing speed based on validated input from database entries.
     * @param validator A pre-initialized validator with the reference text
     * @return Flow of typing speed states
     */
    operator fun invoke(validator: WordValidator): Flow<TypingSpeedState>
}