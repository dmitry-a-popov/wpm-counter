package com.dapsoft.wpmcounter.common.validation

import javax.inject.Inject

/**
 * Compares typed text against sample text word by word.
 *
 * Tokenization rules:
 * 1. Leading/trailing whitespace is ignored.
 * 2. Consecutive whitespace is treated as a single delimiter.
 * 3. Comparison is exact (case-sensitive, punctuation-sensitive).
 *
 * Returns a list where each element represents the comparison of the typed word
 * at a given position against the sample word at the same position.
 * If the sample has fewer words, remaining typed words are marked as non-matching.
 * If the typed text is blank, returns an empty list.
 */
internal class TextValidatorImpl @Inject constructor() : TextValidator {

    override fun compareWords(sampleText: String, typedText: String): List<WordComparison> {
        if (typedText.isBlank()) return emptyList()

        val sampleWords = sampleText.trim().split(WHITESPACE_REGEX)
        val typedWords = typedText.trim().split(WHITESPACE_REGEX)

        return typedWords.mapIndexed { index, typed ->
            val expected = sampleWords.getOrNull(index)
            WordComparison(
                matches = expected == typed
            )
        }
    }

    companion object {
        private val WHITESPACE_REGEX = Regex("\\s+")
    }
}
