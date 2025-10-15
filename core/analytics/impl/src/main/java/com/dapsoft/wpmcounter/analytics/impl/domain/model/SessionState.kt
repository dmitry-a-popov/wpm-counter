package com.dapsoft.wpmcounter.analytics.impl.domain.model

data class SessionState(
    val timestamp: Long = 0L,
    val totalActiveTypingTimeMillis: Long = 0L,
    val validWordCount: Int = 0,
    val currentWord: String = ""
)
