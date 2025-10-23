package com.dapsoft.wpmcounter.common.validation

/**
 * Compares typed text against sample text word by word.
 */
interface TextValidator {

    fun compareWords(sampleText: String, typedText: String): List<WordComparison>
}
