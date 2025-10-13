package com.dapsoft.wpmcounter.analytics.impl.domain

internal interface WordBuffer {
    fun appendSymbolToCurrentWord(symbol: Char)
    fun clearCurrentWord()
    fun getCurrentWord(): String
}