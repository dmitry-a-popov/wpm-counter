package com.dapsoft.wpmcounter.analytics.impl.domain.model

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

data class SessionState(
    val timestamp: Duration = 0.milliseconds,
    val totalActiveTypingTimeMillis: Duration = 0.milliseconds,
    val validWordCount: Int = 0,
    val currentWord: String = ""
)
