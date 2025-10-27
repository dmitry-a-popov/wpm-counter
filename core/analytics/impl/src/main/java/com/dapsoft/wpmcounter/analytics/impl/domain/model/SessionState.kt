package com.dapsoft.wpmcounter.analytics.impl.domain.model

import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Aggregated typing session state.
 *
 * @property lastEventTimestamp The timestamp (same time base used for incoming events) of the last processed keystroke or null if nothing processed.
 *                     Used to compute deltas between consecutive events.
 * @property totalActiveTypingTime Cumulative active typing duration (excludes pauses beyond threshold).
 * @property currentText Characters typed since restart.
 */
@OptIn(ExperimentalTime::class)
internal data class SessionState(
    val lastEventTimestamp: Instant?,
    val totalActiveTypingTime: Duration,
    val currentText: String
) {
    companion object {
        fun initial() = SessionState(
            lastEventTimestamp = null,
            totalActiveTypingTime = Duration.ZERO,
            currentText = ""
        )
    }
}
