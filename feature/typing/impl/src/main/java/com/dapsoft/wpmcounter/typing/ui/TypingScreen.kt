package com.dapsoft.wpmcounter.typing.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import com.dapsoft.wpmcounter.typing.presentation.TypingEffect
import com.dapsoft.wpmcounter.typing.presentation.TypingInputState
import com.dapsoft.wpmcounter.typing.presentation.TypingIntent
import com.dapsoft.wpmcounter.typing.presentation.TypingViewModel

import java.util.Locale

private const val OUTER_PADDING_DP = 16
private const val TEXT_FIELD_HEIGHT_DP = 180
private const val HEADER_FONT_SIZE_SP = 24
private const val SECTION_SPACER_DP = 16
private const val SMALL_SPACER_DP = 8

@Composable
internal fun TypingScreen(
    viewModel: TypingViewModel,
    onChangeUser: () -> Unit,
    textMarker: TextMarker
) {

    LaunchedEffect(key1 = viewModel) {
        viewModel.sideEffect.collect {
            when (it) {
                is TypingEffect.LeaveScreen -> onChangeUser()
            }
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val annotatedSampleText = remember(uiState.sampleText, uiState.currentWordRange) {
        textMarker.markCurrentWord(uiState.sampleText, uiState.currentWordRange)
    }

    val annotatedMistakes = remember(uiState.typedText, uiState.mistakeRanges) {
        textMarker.markMistakes(uiState.typedText, uiState.mistakeRanges)
    }

    val formattedWordsPerMinute = remember(uiState.wordsPerMinute) {
        String.format(Locale.US, "%.2f", uiState.wordsPerMinute)
    }

    val textFieldValue = TextFieldValue(
        text = uiState.typedText,
        selection = TextRange(uiState.typedText.length)
    )

    val columnScroll = rememberScrollState()
    val inputScroll = rememberScrollState()

    Column(
        Modifier
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(OUTER_PADDING_DP.dp)
            .verticalScroll(columnScroll)
    ) {
        Text(
            text = "Speed (WPM): $formattedWordsPerMinute",
            modifier = Modifier
                .fillMaxWidth()
                .semantics { liveRegion = LiveRegionMode.Polite },
            fontSize = HEADER_FONT_SIZE_SP.sp
        )
        Spacer(Modifier.height(SMALL_SPACER_DP.dp))
        Text(
            text = uiState.inputState.toDisplayText(),
            modifier = Modifier.fillMaxWidth(),
            fontSize = HEADER_FONT_SIZE_SP.sp
        )
        Spacer(Modifier.height(SECTION_SPACER_DP.dp))
        Text("Hello, ${uiState.userName}, please type the following text:")
        Spacer(Modifier.height(SECTION_SPACER_DP.dp))
        Text(
            text = annotatedSampleText
        )
        Spacer(Modifier.height(SECTION_SPACER_DP.dp))
        BasicTextField(
            value = textFieldValue,
            onValueChange = { newValue ->
                val forcedEndCursor = TextFieldValue(
                    text = newValue.text,
                    selection = TextRange(newValue.text.length)
                )
                viewModel.dispatch(TypingIntent.ChangeTypedText(forcedEndCursor.text))
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Password //To avoid suggestions
            ),
            visualTransformation = VisualTransformation {
                TransformedText(annotatedMistakes, OffsetMapping.Identity)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(TEXT_FIELD_HEIGHT_DP.dp)
                .onPreviewKeyEvent {
                    it.key == Key.Backspace
                }
                .alpha(if (!uiState.inputState.isInputEnabled) 0.6f else 1f),
            enabled = uiState.inputState.isInputEnabled,
            decorationBox = { inner ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Box(Modifier.verticalScroll(inputScroll)) { inner() }
                }
            }
        )
        Spacer(Modifier.height(SECTION_SPACER_DP.dp))
        Button(onClick = { viewModel.dispatch(TypingIntent.ChangeUser) } ) {
            Text("Change user")
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = { viewModel.dispatch(TypingIntent.Restart) } ) {
            Text("Restart")
        }
    }
}

internal fun TypingInputState.toDisplayText(): String = when (this) {
    TypingInputState.ACTIVE -> "Active"
    TypingInputState.PAUSED -> "Paused"
    TypingInputState.ERROR -> "Internal Error, Please restart"
    TypingInputState.COMPLETED -> "Completed"
}
