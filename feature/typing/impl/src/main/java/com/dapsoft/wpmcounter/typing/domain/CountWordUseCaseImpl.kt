package com.dapsoft.wpmcounter.typing.domain

internal class CountWordUseCaseImpl : CountWordUseCase {

    override fun invoke(text: String): Int {
        var count = 0
        var inWord = false
        var endsWithDelimiter = false

        for (char in text) {
            val isDelimiter = char == ' ' || char == '\t' || char == '\n' || char == '\r'

            if (!isDelimiter && !inWord) {
                inWord = true
                count++
                endsWithDelimiter = false
            } else if (isDelimiter && inWord) {
                inWord = false
                endsWithDelimiter = true
            } else if (isDelimiter) {
                endsWithDelimiter = true
            }
        }

        if (endsWithDelimiter) {
            count++
        }

        return count
    }
}