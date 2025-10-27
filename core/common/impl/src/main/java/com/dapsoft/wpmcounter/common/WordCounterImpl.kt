package com.dapsoft.wpmcounter.common

import javax.inject.Inject

/**
 * Default whitespace-based word counter.
 * Current definition: sequences of non-whitespace characters separated by one or more Unicode whitespace chars.
 * Returns 0 for blank input.
 */
internal class WordCounterImpl @Inject constructor() : WordCounter {

    override fun count(text: String) : Int {
        if (text.isBlank()) {
            return 0
        }
        return text.trim().split(WHITESPACE_REGEX).size
    }

    companion object {
        private val WHITESPACE_REGEX = "\\s+".toRegex()
    }
}
