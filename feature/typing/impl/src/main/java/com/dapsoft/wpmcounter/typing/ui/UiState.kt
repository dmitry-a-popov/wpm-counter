package com.dapsoft.wpmcounter.typing.ui

internal data class UiState(
    val userName: String,
    val sampleText: String,
    val typedText: String,
    val mistakeIndices: List<Pair<Int, Int>>,
    val wordsPerMinute: Float,
    val isInputDisabled: Boolean,
    val isCalculationPaused: Boolean
)