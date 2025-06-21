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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.dapsoft.wpmcounter.typing.presentation.TypingViewModel
import kotlin.comparisons.then

import kotlin.math.roundToInt

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
            text = "Speed (WPM): ${uiState.wordsPerMinute}",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        Text("Hello, ${uiState.userName}, please type the following text:")
        Spacer(Modifier.height(16.dp))
        Text(uiState.sampleText)
        Spacer(Modifier.height(16.dp))
        BasicTextField(
            value = uiState.typedText,
            onValueChange = { vm.processIntent(UiIntent.ChangeTypedText(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .onKeyEvent {
                    if (it.type == KeyEventType.KeyUp &&
                        it.key != Key.Backspace
                    ) {

                    }
                    true
                },
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
        Spacer(Modifier.height(8.dp))
        Button(onClick = onChangeUser) { Text("Change user") }
    }
}