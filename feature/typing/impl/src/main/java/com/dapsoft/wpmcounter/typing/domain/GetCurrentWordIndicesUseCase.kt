package com.dapsoft.wpmcounter.typing.domain

interface GetCurrentWordIndicesUseCase {

    operator fun invoke(typedText: String, wordNumber: Int): Pair<Int, Int>
}