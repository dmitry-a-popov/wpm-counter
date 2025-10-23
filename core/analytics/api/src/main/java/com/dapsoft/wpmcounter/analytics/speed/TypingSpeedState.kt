package com.dapsoft.wpmcounter.analytics.speed

/**
 * Real-time typing performance states.
 *
 * States:
 * - Active: computed speed and optional supplementary metrics.
 * - Paused: inactivity threshold passed; no current active typing burst.
 * - Failed
 *
 */
sealed interface TypingSpeedState {
    data class Active(val wordsPerMinute: Double) : TypingSpeedState
    object Paused : TypingSpeedState
    object Error : TypingSpeedState
}
