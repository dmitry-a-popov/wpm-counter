package com.dapsoft.wpmcounter.analytics.impl.domain

import kotlin.time.Duration

/**
 * Calculates typing speed in Words Per Minute (WPM).
 *
 * Assumptions:
 * - validWordsCount >= 0
 * - activeTime >= 0
 * - Active time is the cumulative time during which typing was detected.
 */
internal interface SpeedCalculator {
    fun calculateWordsPerMinute(validWordsCount: Int, activeTime: Duration): Double
}
