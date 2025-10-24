package com.dapsoft.wpmcounter.login.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.dapsoft.wpmcounter.login.presentation.LoginViewModel

@Composable
internal fun LoginScreen(
    vm: LoginViewModel,
    onLoginConfirmed: (String) -> Unit
) {
    val uiState = vm.uiState.collectAsState().value

    LaunchedEffect(key1 = vm) {
        vm.oneTimeEvent.collect {
            when (it) {
                is OneTimeEvent.LeaveScreen -> {
                    onLoginConfirmed(uiState.userName)
                }
            }
        }
    }

    Column(
        Modifier
        .windowInsetsPadding(WindowInsets.safeDrawing)
        .padding(24.dp)
    ) {
        OutlinedTextField(
            value = uiState.userName,
            onValueChange = { newName ->
                vm.processIntent(UiIntent.ChangeUserName(newName))
            },
            label = { Text("Your name") }
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = { vm.processIntent(UiIntent.ConfirmLogin) }, enabled = uiState.userName.isNotBlank()) {
            Text("Confirm")
        }
    }
}
