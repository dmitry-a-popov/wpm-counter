package com.dapsoft.wpmcounter.common.validation

internal class TextValidatorImpl(private val wordValidator: WordValidator) : TextValidator {

    override fun checkText(sampleText: String, textToCheck: String): List<Boolean> {
        wordValidator.init(sampleText)
        val words = textToCheck.split(" ", "\n", "\r")
        val wordStatuses = words.filter {
            !it.isBlank()
        }.map { wordValidator.isValid(it) }
        return wordStatuses
    }
}