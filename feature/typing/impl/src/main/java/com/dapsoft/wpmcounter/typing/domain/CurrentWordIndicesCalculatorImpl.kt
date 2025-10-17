package com.dapsoft.wpmcounter.typing.domain

class CurrentWordIndicesCalculatorImpl : CurrentWordIndicesCalculator {

    override fun calculate(typedText: String, wordNumber: Int): Pair<Int, Int> {
        var currentWord = 0
        var start = -1

        for ((index, character) in typedText.withIndex()) {
            if (!character.isWhitespace()) {
                if (start == -1) start = index
                if (index == typedText.lastIndex || typedText[index + 1].isWhitespace()) {
                    if (currentWord == wordNumber) {
                        return Pair(start, index + 1)
                    }
                    currentWord++
                    start = -1
                }
            }
        }
        return Pair(0, 0)
    }
}
