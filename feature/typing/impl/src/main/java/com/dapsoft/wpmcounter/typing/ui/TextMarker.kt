package com.dapsoft.wpmcounter.typing.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration

import javax.inject.Inject

class TextMarker @Inject constructor() {

    fun markMistakes(text: String, mistakeIndices: List<Pair<Int, Int>>) = buildAnnotatedString {
        append(text)

        for (startEnd in mistakeIndices) {
                addStyle(
                    style = SpanStyle(
                        color = Color.Red,
                        background = Color.LightGray.copy(alpha = 0.3f)
                    ),
                    start = startEnd.first,
                    end = startEnd.second
                )
        }
    }

    fun markCurrentWord(text: String, wordIndices: Pair<Int, Int>) = buildAnnotatedString {
        append(text)

        addStyle(
            style = SpanStyle(textDecoration = TextDecoration.Underline),
            start = wordIndices.first,
            end = wordIndices.second
        )
    }
}