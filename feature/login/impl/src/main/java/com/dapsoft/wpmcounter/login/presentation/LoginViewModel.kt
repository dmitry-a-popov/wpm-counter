package com.dapsoft.wpmcounter.login.presentation

import com.dapsoft.wpmcounter.logger.Logger
import com.dapsoft.wpmcounter.logger.d
import com.dapsoft.wpmcounter.login.ui.OneTimeEvent
import com.dapsoft.wpmcounter.login.ui.UiIntent
import com.dapsoft.wpmcounter.login.ui.UiState
import com.dapsoft.wpmcounter.ui_common.BaseMviViewModel
import com.dapsoft.wpmcounter.user.SaveUserNameUseCase

import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val saveUserNameUseCase: SaveUserNameUseCase,
    private val log: Logger
) : BaseMviViewModel<UiState, UiIntent, OneTimeEvent>(UiState("")) {

    override suspend fun reduce(intent: UiIntent) {
        log.d(TAG) { "Processing intent: $intent" }
        when (intent) {
            is UiIntent.ChangeUserName -> setState { it.copy(userName = intent.name) }
            is UiIntent.ConfirmLogin -> {
                saveUserNameUseCase(uiState.value.userName.trim())
                sendEvent(OneTimeEvent.LeaveScreen)
            }
        }
    }

    companion object {
        private val TAG = LoginViewModel::class.java.simpleName
    }
}
