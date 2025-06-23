package com.dapsoft.wpmcounter.typing.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.dapsoft.wpmcounter.typing.presentation.TypingViewModel

@Composable
internal fun TypingScreen(
    vm: TypingViewModel,
    onChangeUser: () -> Unit
) {

    LaunchedEffect(key1 = vm) {
        vm.oneTimeEvent.collect {
            when (it) {
                is OneTimeEvent.LeaveScreen -> {
                    onChangeUser()
                }
            }
        }
    }

    val uiState = vm.uiState.collectAsState().value

    Column(Modifier.padding(16.dp)) {
        Text(
            text = "Speed (WPM): ${uiState.wordsPerMinute}${if (uiState.isCalculationPaused) "PAUSED" else ""}",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 24.sp
        )
        Spacer(Modifier.height(16.dp))
        Text("Hello, ${uiState.userName}, please type the following text:")
        Spacer(Modifier.height(16.dp))
        Text(uiState.sampleText)
        Spacer(Modifier.height(16.dp))
        BasicTextField(
            value = uiState.typedText,
            onValueChange = { newValue ->
                vm.processIntent(UiIntent.ChangeTypedText(newValue))
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = VisualTransformation {
                TransformedText(
                    vm.mistakesMarker.mark(uiState.typedText, uiState.mistakeIndices),
                    OffsetMapping.Identity
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .onPreviewKeyEvent {
                    it.type == KeyEventType.KeyDown && it.key == Key.Backspace
                }
                .onKeyEvent {
                    when (it.type) {
                        KeyEventType.KeyUp -> vm.processIntent(UiIntent.KeyRelease(it.nativeKeyEvent.keyCode, it.nativeKeyEvent.eventTime))
                        KeyEventType.KeyDown -> vm.processIntent(UiIntent.KeyPress(it.nativeKeyEvent.keyCode, it.nativeKeyEvent.eventTime))
                    }
                    false
                }
                .alpha(if (uiState.isInputDisabled) 0.6f else 1f),
            enabled = !uiState.isInputDisabled,
            decorationBox = { innerTextField ->
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
                    val scrollState = rememberScrollState()
                    Box(
                        modifier = Modifier
                            .verticalScroll(scrollState)
                    ) {
                        innerTextField()
                    }
                }
            }
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = { vm.processIntent(UiIntent.ChangeUser) } ) { Text("Change user") }
        Spacer(Modifier.height(16.dp))
        Button(onClick = { vm.processIntent(UiIntent.Restart) } ) { Text("Restart") }
    }
}