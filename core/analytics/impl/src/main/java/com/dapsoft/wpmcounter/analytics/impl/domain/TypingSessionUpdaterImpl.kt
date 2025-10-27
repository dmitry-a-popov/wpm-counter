package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.analytics.impl.domain.model.SessionState

import javax.inject.Inject

import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
internal class TypingSessionUpdaterImpl @Inject constructor(
    private val typingSessionStateStore: TypingSessionStateStore
) : TypingSessionUpdater {

    override fun updateForKeystroke(
        symbol: Char,
        timestamp: Instant,
        pauseThreshold: Duration
    ) : SessionState {
        require(pauseThreshold > Duration.ZERO)

        var nextState = typingSessionStateStore.state

        typingSessionStateStore.update { current ->
            nextState = computeNextState(
                current,
                symbol,
                timestamp,
                pauseThreshold
            )
            nextState
        }
        return nextState
    }

    private fun computeNextState(
        current: SessionState,
        symbol: Char,
        timestamp: Instant,
        pauseThreshold: Duration
    ): SessionState {
        val isFirstEvent = current.lastEventTimestamp == null
        val timeDiff = if (isFirstEvent) Duration.ZERO else timestamp - current.lastEventTimestamp
        val withinThreshold = timeDiff < pauseThreshold
        val newTotalActive = if (withinThreshold) current.totalActiveTypingTime + timeDiff else current.totalActiveTypingTime

        return SessionState(
            lastEventTimestamp = timestamp,
            totalActiveTypingTime = newTotalActive,
            currentText = current.currentText + symbol
        )
    }
}
