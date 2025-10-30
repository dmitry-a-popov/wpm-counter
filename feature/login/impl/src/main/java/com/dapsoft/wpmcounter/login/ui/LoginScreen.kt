package com.dapsoft.wpmcounter.login.ui

import android.widget.Toast

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import com.dapsoft.wpmcounter.login.presentation.LoginEffect
import com.dapsoft.wpmcounter.login.presentation.LoginIntent
import com.dapsoft.wpmcounter.login.presentation.LoginViewModel

/**
 * Login screen (Jetpack Compose) implementing a small MVI loop:
 *  - Subscribes to [LoginViewModel.uiState] to render the current user name.
 *  - Dispatches intents to the ViewModel via [LoginViewModel.dispatch] on text change and confirm.
 *  - Collects single-shot effects from [LoginViewModel.sideEffect] for navigation and transient errors.
 *
 * Navigation: on [LoginEffect.LeaveScreen] invokes [onLoginConfirmed] with the latest user name.
 * Errors: on [LoginEffect.ShowLoginError] shows a Toast.
 *
 * @param viewModel Injected presentation layer ViewModel.
 * @param onLoginConfirmed Callback to proceed after successful login.
 */
@Composable
internal fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginConfirmed: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(key1 = viewModel) {
        viewModel.sideEffect.collect {
            when (it) {
                is LoginEffect.LeaveScreen -> onLoginConfirmed(uiState.userName)
                is LoginEffect.ShowLoginError -> {
                    Toast.makeText(
                        context,
                        "Error during user name save",
                        Toast.LENGTH_SHORT
                    ).show()
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
            onValueChange = { newName -> viewModel.dispatch(LoginIntent.OnUserNameChanged(newName)) },
            label = { Text("Your name") }
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { viewModel.dispatch(LoginIntent.OnLoginConfirmed) },
            enabled = uiState.userName.isNotBlank()
        ) { Text("Confirm") }
    }
}
