package com.dapsoft.wpmcounter.typing.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString

import javax.inject.Inject

class MistakesMarker @Inject constructor() {

    fun mark(text: String, mistakeIndices: List<Pair<Int, Int>>) = buildAnnotatedString {
        append(text)

        for (startEnd in mistakeIndices) {
                addStyle(
                    style = SpanStyle(
                        color = Color.Red,
                        background = Color.LightGray.copy(alpha = 0.3f)),
                    start = startEnd.first,
                    end = startEnd.second
                )

        }
    }
}