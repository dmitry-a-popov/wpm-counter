package com.dapsoft.wpmcounter.common

internal class WordCounterImpl : WordCounter {

    override fun count(text: String) : Int {
        var count = 0
        var inWord = false

        for (char in text) {
            val isDelimiter = char == ' ' || char == '\t' || char == '\n' || char == '\r'

            if (!isDelimiter && !inWord) {
                inWord = true
                count++
            } else if (isDelimiter && inWord) {
                inWord = false
            }
        }

        return count
    }
}