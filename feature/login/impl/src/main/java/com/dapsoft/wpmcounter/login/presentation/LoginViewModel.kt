package com.dapsoft.wpmcounter.login.presentation

import com.dapsoft.wpmcounter.logger.Logger
import com.dapsoft.wpmcounter.logger.d
import com.dapsoft.wpmcounter.ui_common.BaseMviViewModel
import com.dapsoft.wpmcounter.user.SaveUserNameUseCase

import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject

/**
 * ViewModel for the Login feature implementing a lightweight MVI flow.
 *
 * Responsibilities:
 * - Holds immutable form state via [LoginUiState] (current user name input).
 * - Processes user/system intents serialized by [BaseMviViewModel]:
 *   - [LoginIntent.OnUserNameChanged] – updates state with the new input value.
 *   - [LoginIntent.OnLoginConfirmed] – triggers persistence through [SaveUserNameUseCase].
 * - Emits one-time events via [LoginEffect]:
 *   - [LoginEffect.LeaveScreen] on successful save (navigation to next screen).
 *   - [LoginEffect.ShowLoginError] on validation/persistence failure (transient error signal).
 *
 */
@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val saveUserNameUseCase: SaveUserNameUseCase,
    private val log: Logger
) : BaseMviViewModel<LoginUiState, LoginIntent, LoginEffect>(LoginUiState("")) {

    /**
     * Intent reducer (single-threaded). Delegates to intent-specific handlers.
     */
    override suspend fun reduce(intent: LoginIntent) {
        log.d(TAG) { "Processing intent: $intent" }
        when (intent) {
            is LoginIntent.OnUserNameChanged -> setState { it.copy(userName = intent.name) }
            is LoginIntent.OnLoginConfirmed -> {
                saveUserNameUseCase(uiState.value.userName).onSuccess {
                    sendSideEffect(LoginEffect.LeaveScreen)
                }.onFailure {
                    sendSideEffect(LoginEffect.ShowLoginError)
                }
            }
        }
    }

    companion object {
        private val TAG: String = LoginViewModel::class.java.simpleName
    }
}
