package com.dapsoft.wpmcounter.typing.domain

interface CurrentWordIndicesCalculator {

    fun calculate(typedText: String, wordNumber: Int): Pair<Int, Int>
}
