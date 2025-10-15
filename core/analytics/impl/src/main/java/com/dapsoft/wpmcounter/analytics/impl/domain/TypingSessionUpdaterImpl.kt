package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.analytics.impl.domain.model.SessionState
import com.dapsoft.wpmcounter.common.validation.WordValidator

import kotlin.time.Duration

internal class TypingSessionUpdaterImpl(private val typingSessionStateStore: TypingSessionStateStore) : TypingSessionUpdater {

    override fun onEvent(symbol: Char, timestamp: Long, pauseThreshold: Duration, validator: WordValidator) : SessionState {
        val currentState = typingSessionStateStore.state

        val newTimestamp = timestamp
        val timeDiff = if (currentState.timestamp == 0L) 0L else timestamp - currentState.timestamp
        val isWithinPauseThreshold = timeDiff < pauseThreshold.inWholeMilliseconds
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