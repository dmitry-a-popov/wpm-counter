package com.dapsoft.wpmcounter.typing.presentation

import androidx.lifecycle.viewModelScope

import com.dapsoft.wpmcounter.analytics.ClearEventsUseCase
import com.dapsoft.wpmcounter.analytics.speed.GetTypingSpeedUseCase
import com.dapsoft.wpmcounter.analytics.TrackKeyPressUseCase
import com.dapsoft.wpmcounter.analytics.speed.TypingSpeedState
import com.dapsoft.wpmcounter.common.WordCounter
import com.dapsoft.wpmcounter.common.validation.WordValidator
import com.dapsoft.wpmcounter.logger.Logger
import com.dapsoft.wpmcounter.typing.domain.CurrentWordIndicesCalculator
import com.dapsoft.wpmcounter.typing.domain.MistakeIndicesCalculator
import com.dapsoft.wpmcounter.typing.domain.SampleTextRepository
import com.dapsoft.wpmcounter.typing.ui.InputState
import com.dapsoft.wpmcounter.typing.ui.TextMarker
import com.dapsoft.wpmcounter.typing.ui.OneTimeEvent
import com.dapsoft.wpmcounter.typing.ui.UiIntent
import com.dapsoft.wpmcounter.typing.ui.UiState
import com.dapsoft.wpmcounter.ui_common.BaseMviViewModel
import com.dapsoft.wpmcounter.user.SaveUserNameUseCase
import com.dapsoft.wpmcounter.user.UserRepository

import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
internal class TypingViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sampleTextRepository: SampleTextRepository,
    private val saveUserNameUseCase: SaveUserNameUseCase,
    private val clearEventsUseCase: ClearEventsUseCase,
    private val trackKeyPressUseCase: TrackKeyPressUseCase,
    private val getTypingSpeedUseCase: GetTypingSpeedUseCase,
    private val currentWordIndicesCalculator: CurrentWordIndicesCalculator,
    private val mistakeIndicesCalculator: MistakeIndicesCalculator,
    private val wordCounter: WordCounter,
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
        inputState = InputState.PAUSED
    )
) {

    init {
        viewModelScope.launch {
            launch {
                userRepository.name.collect { userName ->
                    if (userName.isNotEmpty()) {
                        _uiState.value = _uiState.value.copy(
                            userName = userName
                        )
                    }
                }
            }

            launch {
                sampleTextRepository.text.collect { sampleText ->
                    _uiState.value = _uiState.value.copy(
                        sampleText = sampleText,
                        currentWordIndices = currentWordIndicesCalculator.calculate(sampleText, 0)
                    )
                    wordValidator.init(sampleText)
                    getTypingSpeedUseCase(wordValidator).collect { speedState ->
                        when {
                            _uiState.value.inputState == InputState.COMPLETED -> {
                                // Do nothing if completed
                            }
                            speedState is TypingSpeedState.Error -> {
                                _uiState.value = _uiState.value.copy(
                                    inputState = InputState.ERROR
                                )
                            }
                            speedState is TypingSpeedState.Active -> {
                                _uiState.value = _uiState.value.copy(
                                    inputState = InputState.ACTIVE,
                                    wordsPerMinute = speedState.wordsPerMinute
                                )
                            }
                            speedState is TypingSpeedState.Inactive -> {
                                _uiState.value = _uiState.value.copy(
                                    inputState = InputState.PAUSED
                                )
                            }
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
                    symbol = intent.text.last(),
                    userName = _uiState.value.userName
                ).onFailure {
                    log.e(TAG, "Failed to track key press: ${it.stackTraceToString()}")
                    _uiState.value = _uiState.value.copy(
                        inputState = InputState.ERROR
                    )
                }

                val currentWordNumber = wordCounter.count(intent.text) - 1 + if (intent.text.lastOrNull()?.isWhitespace() == true) 1 else 0

                _uiState.value = _uiState.value.copy(
                    typedText = intent.text,
                    mistakeIndices = mistakeIndicesCalculator.calculate(
                        _uiState.value.sampleText,
                        intent.text
                    ),
                    currentWordIndices = currentWordIndicesCalculator.calculate(
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
        val newInputState = if (clearEventsUseCase().isSuccess) InputState.PAUSED else InputState.ERROR
        wordValidator.init(_uiState.value.sampleText)
        _uiState.value = _uiState.value.copy(
            typedText = "",
            currentWordIndices = Pair(0, 0),
            mistakeIndices = emptyList(),
            wordsPerMinute = 0f,
            inputState = newInputState
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
