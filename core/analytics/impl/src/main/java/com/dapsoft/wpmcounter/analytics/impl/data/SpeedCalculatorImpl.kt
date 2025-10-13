package com.dapsoft.wpmcounter.analytics.impl.data

import com.dapsoft.wpmcounter.analytics.impl.domain.SpeedCalculator
import com.dapsoft.wpmcounter.logger.Logger

import kotlin.time.Duration.Companion.minutes

internal class SpeedCalculatorImpl(private val logger: Logger) : SpeedCalculator {

    override fun calculateWordPerMinute(validWords: Int, activeTimeMillis: Long): Float {
        val activeTimeMinutes = maxOf(
            activeTimeMillis / 1.minutes.inWholeMilliseconds.toFloat(),
            0.01F
        )
        val result = validWords.toFloat() / activeTimeMinutes
        logger.d(TAG, "Calculating typing speed: validWordCount=$validWords, " +
                "totalActiveTypingTimeMillis=$activeTimeMillis, activeTimeMinutes=$activeTimeMinutes, result=$result")
        return result
    }

    companion object {
        private val TAG = SpeedCalculatorImpl::class.java.name
    }
}