package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.logger.Logger
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

internal class SpeedCalculatorImpl(private val log: Logger) : SpeedCalculator {

    override fun calculateWordsPerMinute(validWordsCount: Int, activeTimeMillis: Duration): Float {
        val activeTimeMinutes = maxOf(
            activeTimeMillis.inWholeMilliseconds / 1.minutes.inWholeMilliseconds.toFloat(),
            0.01F
        )
        val result = validWordsCount.toFloat() / activeTimeMinutes
        log.d(TAG, "Calculating typing speed: validWordCount=$validWordsCount, " +
                "totalActiveTypingTimeMillis=$activeTimeMillis, activeTimeMinutes=$activeTimeMinutes, result=$result")
        return result
    }

    companion object {
        private val TAG = SpeedCalculatorImpl::class.java.name
    }
}