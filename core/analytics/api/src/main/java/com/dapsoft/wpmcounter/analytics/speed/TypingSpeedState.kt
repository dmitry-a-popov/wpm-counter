package com.dapsoft.wpmcounter.analytics.speed

/**
 * Represents the current state of typing speed measurement.
 *
 * @property wordsPerMinute The calculated typing speed in words per minute
 * @property isActive Whether typing measurement is currently active
 */
data class TypingSpeedState(
    val wordsPerMinute: Float,
    val isActive: Boolean
)
