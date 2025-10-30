package com.dapsoft.wpmcounter.typing.presentation

import androidx.compose.runtime.Immutable

@Immutable
internal data class TypingUiState(
    val userName: String,
    val sampleText: String,
    val currentWordRange: IntRange?,
    val typedText: String,
    val mistakeRanges: List<IntRange>,
    val wordsPerMinute: Float,
    val inputState: TypingInputState
) {
    companion object {
        fun initial() = TypingUiState(
            userName = "",
            sampleText = "",
            currentWordRange = null,
            typedText = "",
            mistakeRanges = emptyList(),
            wordsPerMinute = 0f,
            inputState = TypingInputState.PAUSED
        )
    }
}
