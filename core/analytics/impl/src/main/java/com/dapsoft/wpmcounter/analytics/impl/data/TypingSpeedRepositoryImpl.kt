package com.dapsoft.wpmcounter.analytics.impl.data

import com.dapsoft.wpmcounter.analytics.impl.domain.TypingSpeedRepository

internal class TypingSpeedRepositoryImpl : TypingSpeedRepository {

    override var startTimestamp: Long = 0L

    override var lastTimestamp: Long = 0L

    override var totalActiveTypingTimeMillis: Long = 0L

    override var validWordCount: Int = 0

    private val currentWord = StringBuilder()
    private var currentWordTimeMillis: Long = 0L

    override fun appendSymbolToCurrentWord(symbol: Char) {
        currentWord.append(symbol)
    }

    override fun clearCurrentWord() {
        currentWord.clear()
        currentWordTimeMillis = 0
    }

    override fun getCurrentWord(): String {
        return currentWord.toString()
    }

    override fun clearState() {
        startTimestamp = 0L
        lastTimestamp = 0L
        totalActiveTypingTimeMillis = 0L
        validWordCount = 0
        clearCurrentWord()
        currentWordTimeMillis = 0L
    }
}