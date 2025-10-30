package com.dapsoft.wpmcounter.typing.domain

/**
 * Calculates character index ranges (start inclusive, end exclusive) of mistyped words
 * in [typedText] when compared to [sampleText].
 */
internal interface MistakeIndicesCalculator {

    fun calculate(sampleText: String, typedText: String): List<IntRange>
}
