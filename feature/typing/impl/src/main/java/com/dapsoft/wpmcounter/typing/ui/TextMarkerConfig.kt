package com.dapsoft.wpmcounter.typing.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration

data class TextMarkerConfig(
    val mistakeForeground: Color = Color.Red,
    val mistakeBackground: Color = Color.LightGray.copy(alpha = 0.3f),
    val currentWordDecoration: TextDecoration = TextDecoration.Underline
)
