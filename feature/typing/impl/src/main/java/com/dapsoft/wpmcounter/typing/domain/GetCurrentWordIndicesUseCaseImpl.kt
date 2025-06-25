package com.dapsoft.wpmcounter.typing.domain

class GetCurrentWordIndicesUseCaseImpl : GetCurrentWordIndicesUseCase {

    override fun invoke(typedText: String, wordNumber: Int): Pair<Int, Int> {
        var currentWordNumber = 0
        var inWord = false
        var startIndex = 0

        for (i in typedText.indices) {
            val char = typedText[i]
            val isDelimiter = char == ' ' || char == '\t' || char == '\n' || char == '\r'

            if (!isDelimiter && !inWord) {
                inWord = true
                startIndex = i
            } else if (isDelimiter && inWord) {
                inWord = false
                currentWordNumber++

                if (currentWordNumber == wordNumber) {
                    return Pair(startIndex, i)
                }
            }
        }

        if (inWord && currentWordNumber + 1 == wordNumber) {
            return Pair(startIndex, typedText.length)
        }

        return Pair(0, 0)
    }
}