package com.dapsoft.wpmcounter.typing.domain

internal interface GetMistakeIndicesUseCase {

    operator fun invoke(sampleText: String, typedText: String): List<Pair<Int, Int>>
}