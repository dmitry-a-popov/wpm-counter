package com.dapsoft.wpmcounter.login.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import com.dapsoft.wpmcounter.login.presentation.LoginEffect
import com.dapsoft.wpmcounter.login.presentation.LoginIntent
import com.dapsoft.wpmcounter.login.presentation.LoginViewModel
import com.dapsoft.wpmcounter.login.impl.R

/**
 * Login screen (Jetpack Compose) implementing a small MVI loop:
 *  - Subscribes to [LoginViewModel.uiState] to render the current user name.
 *  - Dispatches intents to the ViewModel via [LoginViewModel.dispatch] on text change and confirm.
 *  - Collects single-shot effects from [LoginViewModel.sideEffect] for navigation and transient errors.
 *
 * Navigation: on [LoginEffect.LeaveScreen] invokes [onLoginConfirmed] with the latest user name.
 * Errors: on [LoginEffect.ShowLoginError] shows a Material3 Snackbar.
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

    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessage = stringResource(id = R.string.login_error_user_save)

    LaunchedEffect(key1 = viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is LoginEffect.LeaveScreen -> onLoginConfirmed(uiState.userName)
                is LoginEffect.ShowLoginError -> {
                    snackbarHostState.showSnackbar(message = errorMessage)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            OutlinedTextField(
                value = uiState.userName,
                onValueChange = { newName -> viewModel.dispatch(LoginIntent.OnUserNameChanged(newName)) },
                label = { Text(stringResource(id = R.string.login_label_user_name)) }
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { viewModel.dispatch(LoginIntent.OnLoginConfirmed) },
                enabled = uiState.userName.isNotBlank()
            ) { Text(stringResource(id = R.string.login_confirm)) }
        }
    }
}
