package com.dapsoft.wpmcounter.typing.domain

import javax.inject.Inject

internal class CurrentWordIndicesCalculatorImpl @Inject constructor() : CurrentWordIndicesCalculator {

    override fun calculate(text: String, wordNumber: Int): IntRange? {
        if (text.isEmpty()) {
            return null
        }
        var currentWord = 0
        var start = -1

        for ((index, character) in text.withIndex()) {
            if (!character.isWhitespace()) {
                if (start == -1) start = index
                if (index == text.lastIndex || text[index + 1].isWhitespace()) {
                    if (currentWord == wordNumber) {
                        return start until index + 1
                    }
                    currentWord++
                    start = -1
                }
            }
        }
        return null
    }
}
