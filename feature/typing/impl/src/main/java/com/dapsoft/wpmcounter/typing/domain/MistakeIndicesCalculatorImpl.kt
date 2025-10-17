package com.dapsoft.wpmcounter.typing.domain

class MistakeIndicesCalculatorImpl : MistakeIndicesCalculator {

    override fun calculate(sampleText: String, typedText: String): List<Pair<Int, Int>> {
        val typedWords = mutableListOf<Pair<String, Pair<Int, Int>>>()
        var wordStart = -1

        for (i in typedText.indices) {
            val char = typedText[i]
            if (char.isWhitespace()) {
                if (wordStart != -1) {
                    typedWords.add(Pair(typedText.substring(wordStart, i), Pair(wordStart, i)))
                    wordStart = -1
                }
            } else if (wordStart == -1) {
                wordStart = i
            }
        }

        if (wordStart != -1) {
            typedWords.add(Pair(typedText.substring(wordStart), Pair(wordStart, typedText.length)))
        }

        val sampleWords = sampleText.split(Regex("\\s+")).filter { it.isNotEmpty() }

        if (typedWords.isEmpty()) {
            return emptyList()
        }

        val mistakeIntervals = mutableListOf<Pair<Int, Int>>()

        val minSize = minOf(sampleWords.size, typedWords.size)
        for (i in 0 until minSize) {
            if (typedWords[i].first != sampleWords[i]) {
                mistakeIntervals.add(typedWords[i].second)
            }
        }

        if (typedWords.size > sampleWords.size) {
            for (i in sampleWords.size until typedWords.size) {
                mistakeIntervals.add(typedWords[i].second)
            }
        }

        return mistakeIntervals
    }

}
