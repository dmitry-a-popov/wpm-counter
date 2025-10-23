package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.analytics.impl.domain.model.SessionState

import kotlin.time.Duration

internal interface TypingSessionUpdater {

    /**
     * Applies a keystroke to the typing session and returns the new session state.
     * Side-effect: persists the new state internally.
     *
     * Assumptions:
     * - First keystroke does not contribute to active typing time.
     * - Whitespace terminates a word; punctuation is treated as part of a word.
     */
    fun updateForKeystroke(
        symbol: Char,
        timestamp: Duration,
        pauseThreshold: Duration
    ): SessionState
}
