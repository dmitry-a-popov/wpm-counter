package com.dapsoft.wpmcounter.typing.domain

internal interface MistakeIndicesCalculator {

    fun calculate(sampleText: String, typedText: String): List<Pair<Int, Int>>
}
