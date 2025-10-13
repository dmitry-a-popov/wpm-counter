package com.dapsoft.wpmcounter.analytics.impl.domain

interface SpeedCalculator {
    fun calculateWordPerMinute(validWords: Int, activeTimeMillis: Long): Float
}