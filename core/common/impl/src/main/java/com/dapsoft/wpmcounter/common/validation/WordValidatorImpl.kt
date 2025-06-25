package com.dapsoft.wpmcounter.common.validation

import com.dapsoft.wpmcounter.logger.Logger

internal class WordValidatorImpl(private val log: Logger) : WordValidator {

    private lateinit var words: List<String>
    private var currentWordIndex = 0

    override fun init(sampleText: String) {
        words = sampleText.split(" ", "\n", "\r")
            .filter { it.isNotBlank() }

        currentWordIndex = 0
    }

    override fun isValid(word: String): Boolean {
        log.d(TAG, "Validating word: $word, current index: $currentWordIndex, total words: ${words.size}")
        if (words.isEmpty() || currentWordIndex >= words.size) {
            log.d(TAG, "No more words to validate or index out of bounds")
            return false
        }

        log.d(TAG, "Current required word: ${words[currentWordIndex]}")
        if (words[currentWordIndex] == word) {
            currentWordIndex++
            return true
        }

        currentWordIndex++
        return false
    }

    companion object {
        private val TAG = WordValidatorImpl::class.java.simpleName
    }
}