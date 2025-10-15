package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.analytics.impl.domain.model.SessionState
import com.dapsoft.wpmcounter.common.validation.WordValidator

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

internal class TypingSessionUpdaterImpl(private val typingSessionStateStore: TypingSessionStateStore) : TypingSessionUpdater {

    override fun onEvent(symbol: Char, timestamp: Duration, pauseThreshold: Duration, validator: WordValidator) : SessionState {
        val currentState = typingSessionStateStore.state

        val newTimestamp = timestamp
        val timeDiff = if (currentState.timestamp.inWholeMilliseconds == 0L) 0.milliseconds else timestamp.minus(currentState.timestamp)
        val isWithinPauseThreshold = timeDiff < pauseThreshold
        val newTotalActiveTypingTimeMillis = if (isWithinPauseThreshold) currentState.totalActiveTypingTimeMillis + timeDiff else currentState.totalActiveTypingTimeMillis

        var nextWord = currentState.currentWord
        var newValidWordCount = currentState.validWordCount

        if (symbol.isWhitespace()) {
            if (nextWord.isNotEmpty()) {
                if (validator.isValid(nextWord)) {
                    newValidWordCount += 1
                }
                nextWord = ""
            }
        } else {
            nextWord += symbol
        }

        val nextState = SessionState(
            timestamp = newTimestamp,
            totalActiveTypingTimeMillis = newTotalActiveTypingTimeMillis,
            validWordCount = newValidWordCount,
            currentWord = nextWord
        )
        typingSessionStateStore.state = nextState
        return nextState
    }
}