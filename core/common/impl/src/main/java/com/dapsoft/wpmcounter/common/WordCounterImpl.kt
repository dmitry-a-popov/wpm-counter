package com.dapsoft.wpmcounter.common

import com.dapsoft.wpmcounter.common.WordCounter

internal class WordCounterImpl : WordCounter {

    override fun count(text: String) : Int {
        return text.trim().split(Regex("\\s+")).count { it.isNotEmpty() }
    }
}
