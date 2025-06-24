package com.dapsoft.wpmcounter.typing.presentation

import androidx.lifecycle.viewModelScope

import com.dapsoft.wpmcounter.analytics.ClearEventsUseCase
import com.dapsoft.wpmcounter.analytics.GetTypingSpeedUseCase
import com.dapsoft.wpmcounter.analytics.TrackKeyPressUseCase
import com.dapsoft.wpmcounter.common.TimeProvider
import com.dapsoft.wpmcounter.common.screenorientation.ScreenOrientationProvider
import com.dapsoft.wpmcounter.logger.Logger
import com.dapsoft.wpmcounter.typing.domain.GetMistakeIndicesUseCase
import com.dapsoft.wpmcounter.typing.domain.GetSampleTextUseCase
import com.dapsoft.wpmcounter.typing.domain.InputTextValidator
import com.dapsoft.wpmcounter.typing.ui.MistakesMarker
import com.dapsoft.wpmcounter.typing.ui.OneTimeEvent
import com.dapsoft.wpmcounter.typing.ui.UiIntent
import com.dapsoft.wpmcounter.typing.ui.UiState
import com.dapsoft.wpmcounter.ui_common.BaseMviViewModel
import com.dapsoft.wpmcounter.user.GetUserNameUseCase
import com.dapsoft.wpmcounter.user.SaveUserNameUseCase

import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
internal class TypingViewModel @Inject constructor(
    private val getUserNameUseCase: GetUserNameUseCase,
    private val saveUserNameUseCase: SaveUserNameUseCase,
    private val getSampleTextUseCase: GetSampleTextUseCase,
    private val getMistakeIndicesUseCase: GetMistakeIndicesUseCase,
    private val clearEventsUseCase: ClearEventsUseCase,
    private val trackKeyPressUseCase: TrackKeyPressUseCase,
    private val getTypingSpeedUseCase: GetTypingSpeedUseCase,
    private val screenOrientationProvider: ScreenOrientationProvider,
    private val timeProvider: TimeProvider,
    val mistakesMarker: MistakesMarker,
    val log: Logger
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
            launch {
                getUserNameUseCase().collect { userName ->
                    if (userName.isNotEmpty() == true) {
                        _uiState.value = _uiState.value.copy(
                            userName = userName
                        )
                    }
                }
            }

            launch {
                getSampleTextUseCase().collect { sampleText ->
                    _uiState.value = _uiState.value.copy(
                        sampleText = sampleText
                    )
                }
            }

            launch {
                getTypingSpeedUseCase(InputTextValidator()).collect { speed ->
                    _uiState.value = _uiState.value.copy(
                        wordsPerMinute = speed
                    )
                }
            }
        }
    }

    override fun processIntent(intent: UiIntent) = viewModelScope.launch {
        log.d(TAG, "Processing intent: $intent")
        when (intent) {
            UiIntent.ChangeUser -> changeUser()
            is UiIntent.ChangeTypedText -> if (intent.text.length > _uiState.value.typedText.length) {
                trackKeyPressUseCase(
                    keyCode = intent.text.last(),
                    eventTimeMillis = timeProvider.getElapsedRealtime(),
                    phoneOrientation = screenOrientationProvider.getCurrentOrientation(),
                    username = _uiState.value.userName
                )
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

    private suspend fun clearState() {
        clearEventsUseCase()
        _uiState.value = _uiState.value.copy(
            typedText = "",
            mistakeIndices = emptyList(),
            wordsPerMinute = 0f
        )
    }

    private suspend fun changeUser() {
        clearState()
        saveUserNameUseCase("")
        _oneTimeEvent.emit(OneTimeEvent.LeaveScreen)
    }

    companion object {
        private val TAG = TypingViewModel.javaClass.name
    }
}