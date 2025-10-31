package com.dapsoft.wpmcounter.typing.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
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

import com.dapsoft.wpmcounter.typing.impl.R
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

/**
 * High-level typing practice screen.
 *
 * Responsibilities:
 * 1. Collects side effects from [TypingViewModel] (e.g. navigation via `TypingEffect.LeaveScreen`).
 * 2. Observes UI state: WPM, input state, user name, sample text, typed text, mistakes, current word range.
 * 3. Builds annotated sample text and mistake highlighting via [TextMarker].
 * 4. Shows:
 *    - Live WPM (polite accessibility live region).
 *    - Current input state (ACTIVE / PAUSED / ERROR / COMPLETED).
 *    - Greeting with user name.
 *    - Sample text with current word highlighting.
 *    - Multiline input field with:
 *        * Forced cursor at end (cannot move back manually).
 *        * Disabled IME suggestions (Password keyboard type).
 *        * Mistake highlighting through a custom [VisualTransformation].
 *        * `isError` flag when mistakes exist (Material error styling).
 *        * Alpha reduction when input is disabled.
 *    - Buttons to change user and restart session.
 *
 * The column is scrollable to accommodate long sample text. Insets are handled by outer Scaffold.
 *
 * @param viewModel Source of state and side effects.
 * @param onChangeUser Callback triggered when user switching is requested.
 * @param textMarker Provides span annotation for current word and mistakes.
 */
@Composable
internal fun TypingScreen(
    viewModel: TypingViewModel,
    onChangeUser: () -> Unit,
    textMarker: TextMarker
) {

    LaunchedEffect(key1 = viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
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

    val textFieldValue by remember(uiState.typedText) {
        mutableStateOf(
            TextFieldValue(
                text = uiState.typedText,
                selection = TextRange(uiState.typedText.length)
            )
        )
    }

    val mistakeTransformation = remember(annotatedMistakes) {
        VisualTransformation { TransformedText(annotatedMistakes, OffsetMapping.Identity) }
    }

    Scaffold { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                Modifier
                    .padding(OUTER_PADDING_DP.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                HeaderSection(formattedWordsPerMinute, uiState.inputState, uiState.userName)
                Spacer(Modifier.height(SECTION_SPACER_DP.dp))
                SampleTextSection(annotatedSampleText)
                Spacer(Modifier.height(SECTION_SPACER_DP.dp))
                TypingInputField(
                    value = textFieldValue,
                    hasMistakes = uiState.mistakeRanges.isNotEmpty(),
                    mistakeTransformation = mistakeTransformation,
                    enabled = uiState.inputState.isInputEnabled,
                    onChange = { newValue ->
                        viewModel.dispatch(TypingIntent.ChangeTypedText(newValue.text))
                    }
                )
                Spacer(Modifier.height(SECTION_SPACER_DP.dp))
                ActionsSection(
                    onChangeUser = { viewModel.dispatch(TypingIntent.ChangeUser) },
                    onRestart = { viewModel.dispatch(TypingIntent.Restart) }
                )
            }
        }
    }
}

@Composable
private fun HeaderSection(
    wordPerMinute: String,
    inputState: TypingInputState,
    userName: String
) {
    Column {
        Text(
            text = stringResource(id = R.string.typing_speed_wpm, wordPerMinute),
            modifier = Modifier
                .fillMaxWidth()
                .semantics { liveRegion = LiveRegionMode.Polite },
            fontSize = HEADER_FONT_SIZE_SP.sp
        )
        Spacer(Modifier.height(SMALL_SPACER_DP.dp))
        Text(
            text = stringResource(inputState.toDisplayTextRes()),
            modifier = Modifier.fillMaxWidth(),
            fontSize = HEADER_FONT_SIZE_SP.sp
        )
        Spacer(Modifier.height(SECTION_SPACER_DP.dp))
        Text(stringResource(id = R.string.typing_greeting_template, userName))
    }
}

@Composable
private fun SampleTextSection(annotatedSampleText: androidx.compose.ui.text.AnnotatedString) {
    Text(text = annotatedSampleText)
}

@Composable
private fun TypingInputField(
    value: TextFieldValue,
    hasMistakes: Boolean,
    mistakeTransformation: VisualTransformation,
    enabled: Boolean,
    onChange: (TextFieldValue) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(TEXT_FIELD_HEIGHT_DP.dp)
            .alpha(if (enabled) 1f else 0.6f),
        enabled = enabled,
        singleLine = false,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrectEnabled = false,
            keyboardType = KeyboardType.Password // Disable IME suggestions
        ),
        visualTransformation = mistakeTransformation,
        isError = hasMistakes
    )
}

@Composable
private fun ActionsSection(
    onChangeUser: () -> Unit,
    onRestart: () -> Unit
) {
    Button(onClick = onChangeUser) {
        Text(stringResource(id = R.string.typing_change_user))
    }
    Spacer(Modifier.height(16.dp))
    Button(onClick = onRestart) {
        Text(stringResource(id = R.string.typing_restart))
    }
}

internal fun TypingInputState.toDisplayTextRes(): Int = when (this) {
    TypingInputState.ACTIVE -> R.string.typing_state_active
    TypingInputState.PAUSED -> R.string.typing_state_paused
    TypingInputState.ERROR -> R.string.typing_state_error
    TypingInputState.COMPLETED -> R.string.typing_state_completed
}
