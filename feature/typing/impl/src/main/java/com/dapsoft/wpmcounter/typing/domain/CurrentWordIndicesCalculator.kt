package com.dapsoft.wpmcounter.typing.domain

/**
 * Calculates the character index range (start inclusive, end exclusive) of a word by its zero-based index.
 * @return IntRange or null if the word index is out of bounds.
 */
interface CurrentWordIndicesCalculator {

    fun calculate(text: String, wordNumber: Int): IntRange?
}
