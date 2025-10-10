package com.dapsoft.wpmcounter.analytics.impl.data

import com.dapsoft.wpmcounter.analytics.impl.domain.TypingSpeedRepository

internal class TypingSpeedRepositoryImpl : TypingSpeedRepository {

    override var startTimestamp: Long = 0L

    override var lastTimestamp: Long = 0L

    override var totalActiveTypingTimeMillis: Long = 0L

    override var validWordCount: Int = 0

    override fun clearState() {
        startTimestamp = 0L
        lastTimestamp = 0L
        totalActiveTypingTimeMillis = 0L
        validWordCount = 0
    }
}