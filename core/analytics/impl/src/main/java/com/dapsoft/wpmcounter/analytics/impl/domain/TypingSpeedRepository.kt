package com.dapsoft.wpmcounter.analytics.impl.domain

internal interface TypingSpeedRepository {

    var startTimestamp: Long
    var lastTimestamp: Long
    var totalActiveTypingTimeMillis: Long
    var validWordCount: Int

    fun appendSymbolToCurrentWord(symbol: Char)
    fun clearCurrentWord()
    fun getCurrentWord(): String

    fun addTimeToCurrentWord(timeMillis: Long)
    fun getCurrentWordTypingTime(): Long
}