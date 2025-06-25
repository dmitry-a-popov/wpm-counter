package com.dapsoft.wpmcounter.typing.domain

internal interface CountWordUseCase {
    operator fun invoke(text: String): Int
}