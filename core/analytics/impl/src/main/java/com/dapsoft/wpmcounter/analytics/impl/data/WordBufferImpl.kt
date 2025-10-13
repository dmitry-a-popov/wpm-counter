package com.dapsoft.wpmcounter.analytics.impl.data

import com.dapsoft.wpmcounter.analytics.impl.domain.WordBuffer

internal class WordBufferImpl : WordBuffer {

    private val currentWord = StringBuilder()

    override fun appendSymbolToCurrentWord(symbol: Char) {
        currentWord.append(symbol)
    }

    override fun clearCurrentWord() {
        currentWord.clear()
    }

    override fun getCurrentWord(): String {
        return currentWord.toString()
    }
}