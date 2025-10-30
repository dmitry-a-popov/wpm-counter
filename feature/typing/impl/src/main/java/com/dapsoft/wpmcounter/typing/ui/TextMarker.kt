package com.dapsoft.wpmcounter.typing.ui

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString

import javax.inject.Inject

/**
 * Utility for producing highlighted text for typing feedback.
 * All indices are assumed 0-based and inclusive ranges.
 */
internal class TextMarker @Inject constructor(
    private val config: TextMarkerConfig
) {

    fun markMistakes(text: String, mistakeRanges: List<IntRange>) = buildAnnotatedString {
        append(text)

        for (range in mistakeRanges) {
                addStyle(
                    style = SpanStyle(
                        color = config.mistakeForeground,
                        background = config.mistakeBackground
                    ),
                    start = range.first,
                    end = range.last + 1
                )
        }
    }

    fun markCurrentWord(text: String, wordIndices: IntRange?) = buildAnnotatedString {
        append(text)

        wordIndices?.let {
            addStyle(
                style = SpanStyle(textDecoration = config.currentWordDecoration),
                start = it.first,
                end = it.last + 1
            )
        }
    }
}
