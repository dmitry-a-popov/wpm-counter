package com.dapsoft.wpmcounter.common.validation

interface TextValidator {

    fun checkText(sampleText: String, text: String): List<Boolean>
}