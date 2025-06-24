package com.dapsoft.wpmcounter.login.presentation

import androidx.lifecycle.viewModelScope

import com.dapsoft.wpmcounter.logger.Logger
import com.dapsoft.wpmcounter.login.ui.OneTimeEvent
import com.dapsoft.wpmcounter.login.ui.UiIntent
import com.dapsoft.wpmcounter.login.ui.UiState
import com.dapsoft.wpmcounter.ui_common.BaseMviViewModel
import com.dapsoft.wpmcounter.user.SaveUserNameUseCase

import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val saveUserNameUseCase: SaveUserNameUseCase,
    private val log: Logger
) : BaseMviViewModel<UiState, UiIntent, OneTimeEvent>(UiState("")) {

    override fun processIntent(intent: UiIntent) = viewModelScope.launch {
        log.d(TAG, "Processing intent: $intent")
        when (intent) {
            is UiIntent.ChangeUserName -> _uiState.emit(UiState(intent.name))
            UiIntent.ConfirmLogin -> confirm()
        }
    }

    private suspend fun confirm() {
        saveUserNameUseCase(uiState.value.userName.trim())
        _oneTimeEvent.emit(OneTimeEvent.LeaveScreen)
    }

    companion object {
        private val TAG = LoginViewModel.javaClass.name
    }
}