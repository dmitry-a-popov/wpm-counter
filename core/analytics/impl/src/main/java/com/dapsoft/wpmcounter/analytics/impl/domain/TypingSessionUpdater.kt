package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.analytics.impl.domain.model.SessionState
import com.dapsoft.wpmcounter.common.validation.WordValidator

import kotlin.time.Duration

internal interface TypingSessionUpdater {
    fun onEvent(symbol: Char, timestamp: Duration, pauseThreshold: Duration, validator: WordValidator): SessionState
}