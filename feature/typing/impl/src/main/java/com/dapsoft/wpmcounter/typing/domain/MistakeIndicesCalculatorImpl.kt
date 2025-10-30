package com.dapsoft.wpmcounter.typing.domain

import com.dapsoft.wpmcounter.common.validation.TextValidator

import javax.inject.Inject

internal class MistakeIndicesCalculatorImpl @Inject constructor(
    private val textValidator: TextValidator
) : MistakeIndicesCalculator {

    override fun calculate(sampleText: String, typedText: String): List<IntRange> {
        val typedWordIntervals = mutableListOf<IntRange>()
        var wordStart = -1

        for (i in typedText.indices) {
            if (typedText[i].isWhitespace()) {
                if (wordStart != -1) {
                    typedWordIntervals.add(wordStart until i)
                    wordStart = -1
                }
            } else if (wordStart == -1) {
                wordStart = i
            }
        }

        if (wordStart != -1) {
            typedWordIntervals.add(wordStart until typedText.length)
        }

        val comparisons = textValidator.compareWords(sampleText, typedText)

        return typedWordIntervals
            .zip(comparisons)
            .filter { (_, comparison) -> !comparison.matches }
            .map { (interval, _) -> interval }
    }

}
