package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.logger.Logger
import com.dapsoft.wpmcounter.logger.d

import javax.inject.Inject

import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

internal class SpeedCalculatorImpl @Inject constructor(private val log: Logger) : SpeedCalculator {

    override fun calculateWordsPerMinute(validWordsCount: Int, activeTime: Duration): Float {
        require(validWordsCount >= 0) { "validWordsCount must be >= 0" }
        require(!activeTime.isNegative()) { "activeTime must be >= 0" }

        if (validWordsCount == 0 || activeTime == Duration.ZERO) {
            log.d(TAG) { "No words or zero active time. Returning 0 WPM" }
            return 0.toFloat()
        }

        val activeTimeMinutes = activeTime.inWholeMilliseconds.toFloat() / ONE_MINUTE_MILLIS
        if (activeTimeMinutes < MIN_ACTIVE_MINUTES_THRESHOLD) {
            log.d(TAG) { "Active time below threshold ($MIN_ACTIVE_MINUTES_THRESHOLD minutes). Returning 0 WPM" }
            return 0.toFloat()
        }

        val result = validWordsCount.toFloat() / activeTimeMinutes
        log.d(TAG) {
            "Calculating typing speed: validWordCount=$validWordsCount, totalActiveTypingTimeMillis=$activeTime, activeTimeMinutes=$activeTimeMinutes, result=$result"
        }
        return result
    }

    companion object {
        private val TAG = SpeedCalculatorImpl::class.java.simpleName
        private val ONE_MINUTE_MILLIS = 1.minutes.inWholeMilliseconds.toFloat()
        private const val MIN_ACTIVE_MINUTES_THRESHOLD = 0.001 // ~60ms
    }
}
