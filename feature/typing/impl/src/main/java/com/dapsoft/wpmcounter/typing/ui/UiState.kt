package com.dapsoft.wpmcounter.typing.ui

internal data class UiState(
    val userName: String,
    val sampleText: String,
    val currentWordIndices: Pair<Int, Int>,
    val typedText: String,
    val mistakeIndices: List<Pair<Int, Int>>,
    val wordsPerMinute: Float,
    val inputState: InputState
)

enum class InputState {
    ACTIVE,
    PAUSED,
    COMPLETED;

    val isInputEnabled: Boolean
        get() = this == ACTIVE || this == PAUSED

    val displayText: String
        get() = when (this) {
            ACTIVE -> "Active"
            PAUSED -> "Paused"
            COMPLETED -> "Completed"
        }
}
