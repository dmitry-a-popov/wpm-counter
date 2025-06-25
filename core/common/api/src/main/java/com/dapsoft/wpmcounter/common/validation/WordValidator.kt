package com.dapsoft.wpmcounter.common.validation

interface WordValidator {

    fun init(sampleText: String)

    fun isValid(word: String): Boolean
}