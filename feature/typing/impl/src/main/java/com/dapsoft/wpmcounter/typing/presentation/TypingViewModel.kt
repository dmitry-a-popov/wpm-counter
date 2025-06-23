package com.dapsoft.wpmcounter.typing.presentation

import androidx.lifecycle.viewModelScope

import com.dapsoft.wpmcounter.typing.domain.GetMistakeIndicesUseCase
import com.dapsoft.wpmcounter.typing.domain.GetSampleTextUseCase
import com.dapsoft.wpmcounter.typing.ui.MistakesMarker
import com.dapsoft.wpmcounter.typing.ui.OneTimeEvent
import com.dapsoft.wpmcounter.typing.ui.UiIntent
import com.dapsoft.wpmcounter.typing.ui.UiState
import com.dapsoft.wpmcounter.ui_common.BaseMviViewModel
import com.dapsoft.wpmcounter.user.GetUserNameUseCase
import com.dapsoft.wpmcounter.user.SaveUserNameUseCase

import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
internal class TypingViewModel @Inject constructor(
    private val getUserNameUseCase: GetUserNameUseCase,
    private val saveUserNameUseCase: SaveUserNameUseCase,
    private val getSampleTextUseCase: GetSampleTextUseCase,
    private val getMistakeIndicesUseCase: GetMistakeIndicesUseCase,
    val mistakesMarker: MistakesMarker
) : BaseMviViewModel<UiState, UiIntent, OneTimeEvent>(
    UiState(
        userName = "",
        sampleText = "",
        typedText = "",
        mistakeIndices = emptyList(),
        wordsPerMinute = 0F,
        isInputDisabled = false,
        isCalculationPaused = false
    )
) {

    init {
        viewModelScope.launch {
            getUserNameUseCase().collect { userName ->
                if (userName.isNotEmpty() == true) {
                    _uiState.value = _uiState.value.copy(
                        userName = userName
                    )
                }
            }
        }

        viewModelScope.launch {
            getSampleTextUseCase().collect { sampleText ->
                _uiState.value = _uiState.value.copy(
                    sampleText = sampleText
                )
            }
        }
    }

    override fun processIntent(intent: UiIntent) = viewModelScope.launch {
        when (intent) {
            UiIntent.ChangeUser -> changeUser()
            is UiIntent.ChangeTypedText -> if (intent.text.length > _uiState.value.typedText.length) {
                _uiState.value = _uiState.value.copy(
                    typedText = intent.text,
                    mistakeIndices = getMistakeIndicesUseCase(
                        _uiState.value.sampleText,
                        intent.text
                    ),
                )
            }

            UiIntent.Restart -> clearState()
        }
    }

    //TODO mark isFinished as true
    //TODO mark isInputDisabled as true

    private fun clearState() {
        _uiState.value = _uiState.value.copy(
            typedText = "",
            mistakeIndices = emptyList(),
            wordsPerMinute = 0f
        )
    }

    private suspend fun changeUser() {
        saveUserNameUseCase.invoke("")
        _oneTimeEvent.emit(OneTimeEvent.LeaveScreen)
    }
}