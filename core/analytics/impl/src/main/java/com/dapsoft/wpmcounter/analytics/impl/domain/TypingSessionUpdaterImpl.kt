package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.analytics.impl.domain.model.SessionState
import com.dapsoft.wpmcounter.common.validation.WordValidator

import javax.inject.Inject

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

internal class TypingSessionUpdaterImpl @Inject constructor(
    private val typingSessionStateStore: TypingSessionStateStore
) : TypingSessionUpdater {

    override fun updateForKeystroke(
        symbol: Char,
        timestamp: Duration,
        pauseThreshold: Duration,
        validator: WordValidator
    ) : SessionState {
        require(pauseThreshold > Duration.ZERO)

        var nextState = typingSessionStateStore.state

        typingSessionStateStore.update { current ->
            nextState = computeNextState(
                current,
                symbol,
                timestamp,
                pauseThreshold,
                validator
            )
            nextState
        }
        return nextState
    }

    private fun computeNextState(
        current: SessionState,
        symbol: Char,
        timestamp: Duration,
        pauseThreshold: Duration,
        validator: WordValidator
    ): SessionState {
        val isFirstEvent = current.lastEventTimestamp.inWholeMilliseconds == 0L
        val timeDiff = if (isFirstEvent) 0.milliseconds else timestamp - current.lastEventTimestamp
        val withinThreshold = timeDiff < pauseThreshold
        val newTotalActive = if (withinThreshold) current.totalActiveTypingTime + timeDiff else current.totalActiveTypingTime

        var word = current.currentWord
        var validCount = current.validWordCount
        if (symbol.isWhitespace()) {
            if (word.isNotEmpty()) {
                if (validator.isValid(word)) validCount += 1
                word = ""
            }
        } else {
            word += symbol
        }

        return SessionState(
            lastEventTimestamp = timestamp,
            totalActiveTypingTime = newTotalActive,
            validWordCount = validCount,
            currentWord = word
        )
    }
}
