package com.dapsoft.wpmcounter.typing.domain

import com.dapsoft.wpmcounter.common.validation.TextValidator

internal class MistakeIndicesCalculatorImpl(private val textValidator: TextValidator) : MistakeIndicesCalculator {

    override fun calculate(sampleText: String, typedText: String): List<Pair<Int, Int>> {
        val typedWordIntervals = mutableListOf<Pair<Int, Int>>()
        var wordStart = -1

        for (i in typedText.indices) {
            if (typedText[i].isWhitespace()) {
                if (wordStart != -1) {
                    typedWordIntervals.add(wordStart to i)
                    wordStart = -1
                }
            } else if (wordStart == -1) {
                wordStart = i
            }
        }

        if (wordStart != -1) {
            typedWordIntervals.add(wordStart to typedText.length)
        }

        val comparisons = textValidator.compareWords(sampleText, typedText)

        return typedWordIntervals
            .zip(comparisons)
            .filter { (_, comparison) -> !comparison.matches }
            .map { (interval, _) -> interval }
    }

}
