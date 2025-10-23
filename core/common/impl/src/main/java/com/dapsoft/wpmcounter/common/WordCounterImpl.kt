package com.dapsoft.wpmcounter.common

internal class WordCounterImpl : WordCounter {

    override fun count(text: String) : Int {
        return text.trim().split(Regex("\\s+")).count { it.isNotEmpty() }
    }
}
