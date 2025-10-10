package com.dapsoft.wpmcounter.typing.presentation

import androidx.lifecycle.viewModelScope

import com.dapsoft.wpmcounter.analytics.ClearEventsUseCase
import com.dapsoft.wpmcounter.analytics.speed.GetTypingSpeedUseCase
import com.dapsoft.wpmcounter.analytics.TrackKeyPressUseCase
import com.dapsoft.wpmcounter.common.TimeProvider
import com.dapsoft.wpmcounter.common.WordCounter
import com.dapsoft.wpmcounter.common.orientation.ScreenOrientationProvider
import com.dapsoft.wpmcounter.common.validation.WordValidator
import com.dapsoft.wpmcounter.logger.Logger
import com.dapsoft.wpmcounter.typing.domain.GetCurrentWordIndicesUseCase
import com.dapsoft.wpmcounter.typing.domain.GetMistakeIndicesUseCase
import com.dapsoft.wpmcounter.typing.domain.GetSampleTextUseCase
import com.dapsoft.wpmcounter.typing.ui.InputState
import com.dapsoft.wpmcounter.typing.ui.TextMarker
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
    private val clearEventsUseCase: ClearEventsUseCase,
    private val trackKeyPressUseCase: TrackKeyPressUseCase,
    private val getTypingSpeedUseCase: GetTypingSpeedUseCase,
    private val getCurrentWordIndicesUseCase: GetCurrentWordIndicesUseCase,
    private val screenOrientationProvider: ScreenOrientationProvider,
    private val wordCounter: WordCounter,
    private val timeProvider: TimeProvider,
    private val wordValidator: WordValidator,
    val textMarker: TextMarker,
    val log: Logger
) : BaseMviViewModel<UiState, UiIntent, OneTimeEvent>(
    UiState(
        userName = "",
        sampleText = "",
        currentWordIndices = Pair(0, 0),
        typedText = "",
        mistakeIndices = emptyList(),
        wordsPerMinute = 0F,
        inputState = InputState.ACTIVE
    )
) {

    init {
        viewModelScope.launch {
            launch {
                getUserNameUseCase().collect { userName ->
                    if (userName.isNotEmpty()) {
                        _uiState.value = _uiState.value.copy(
                            userName = userName
                        )
                    }
                }
            }

            launch {
                getSampleTextUseCase().collect { sampleText ->
                    _uiState.value = _uiState.value.copy(
                        sampleText = sampleText,
                        currentWordIndices = getCurrentWordIndicesUseCase(sampleText, 0)
                    )
                    wordValidator.init(sampleText)
                    getTypingSpeedUseCase(wordValidator).collect { speedState ->
                        _uiState.value = _uiState.value.copy(
                            wordsPerMinute = speedState.wordsPerMinute
                        )

                        if (_uiState.value.inputState != InputState.COMPLETED) {
                            _uiState.value = _uiState.value.copy(
                                inputState = if (speedState.isActive) InputState.ACTIVE else InputState.PAUSED
                            )
                        }
                    }
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

                val currentWordNumber = wordCounter.count(intent.text) - 1 + if (intent.text.lastOrNull()?.isWhitespace() == true) 1 else 0

                _uiState.value = _uiState.value.copy(
                    typedText = intent.text,
                    mistakeIndices = getMistakeIndicesUseCase(
                        _uiState.value.sampleText,
                        intent.text
                    ),
                    currentWordIndices = getCurrentWordIndicesUseCase(
                        _uiState.value.sampleText,
                        currentWordNumber
                    )
                )

                val sampleTextWordNumber = wordCounter.count(_uiState.value.sampleText)

                if (currentWordNumber + 1 > sampleTextWordNumber) {
                    _uiState.value = _uiState.value.copy(
                        inputState = InputState.COMPLETED
                    )
                }
            }
            UiIntent.Restart -> clearState()
        }
    }

    private suspend fun clearState() {
        clearEventsUseCase()
        wordValidator.init(_uiState.value.sampleText)
        _uiState.value = _uiState.value.copy(
            typedText = "",
            currentWordIndices = Pair(0, 0),
            mistakeIndices = emptyList(),
            wordsPerMinute = 0f,
            inputState = InputState.ACTIVE
        )
    }

    private suspend fun changeUser() {
        clearState()
        saveUserNameUseCase("")
        _oneTimeEvent.emit(OneTimeEvent.LeaveScreen)
    }

    companion object {
        private val TAG = TypingViewModel::class.java.simpleName
    }
}