package com.dapsoft.wpmcounter.analytics.speed

sealed class TypingSpeedState {
    data class Active(val wordsPerMinute: Float) : TypingSpeedState()
    object Inactive : TypingSpeedState()
    object Error : TypingSpeedState()
}
