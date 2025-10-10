package com.dapsoft.wpmcounter.analytics.impl.domain

internal interface WordRepository {
    fun appendSymbolToCurrentWord(symbol: Char)
    fun clearCurrentWord()
    fun getCurrentWord(): String
}