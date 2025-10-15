package com.dapsoft.wpmcounter.analytics.impl.domain

import kotlin.time.Duration

interface SpeedCalculator {
    fun calculateWordsPerMinute(validWordsCount: Int, activeTimeMillis: Duration): Float
}