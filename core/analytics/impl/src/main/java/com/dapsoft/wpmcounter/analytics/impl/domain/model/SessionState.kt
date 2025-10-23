package com.dapsoft.wpmcounter.analytics.impl.domain.model

import kotlin.time.Duration

/**
 * Aggregated typing session state.
 *
 * @property lastEventTimestamp The timestamp (same time base used for incoming events) of the last processed keystroke.
 *                     Used to compute deltas between consecutive events.
 * @property totalActiveTypingTime Cumulative active typing duration (excludes pauses beyond threshold).
 * @property validWordCount Count of words accepted as valid so far.
 * @property currentWord Characters typed since last whitespace; empty when between words.
 */
internal data class SessionState(
    val lastEventTimestamp: Duration,
    val totalActiveTypingTime: Duration,
    val validWordCount: Int,
    val currentWord: String
) {
    companion object {
        fun initial() = SessionState(
            lastEventTimestamp = Duration.ZERO,
            totalActiveTypingTime = Duration.ZERO,
            validWordCount = 0,
            currentWord = ""
        )
    }
}
