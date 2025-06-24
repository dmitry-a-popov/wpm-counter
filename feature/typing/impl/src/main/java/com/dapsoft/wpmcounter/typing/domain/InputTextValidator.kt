package com.dapsoft.wpmcounter.typing.domain

import com.dapsoft.wpmcounter.analytics.WordValidator

class InputTextValidator : WordValidator {
    override fun isValidWord(word: String): Boolean {
        return true
    }
}