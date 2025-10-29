package com.dapsoft.wpmcounter.typing.presentation

import androidx.lifecycle.viewModelScope

import com.dapsoft.wpmcounter.analytics.ClearEventsUseCase
import com.dapsoft.wpmcounter.analytics.speed.ObserveTypingSpeedUseCase
import com.dapsoft.wpmcounter.analytics.TrackKeyPressUseCase
import com.dapsoft.wpmcounter.analytics.speed.TypingSpeedState
import com.dapsoft.wpmcounter.common.WordCounter
import com.dapsoft.wpmcounter.logger.Logger
import com.dapsoft.wpmcounter.logger.d
import com.dapsoft.wpmcounter.logger.e
import com.dapsoft.wpmcounter.typing.domain.CurrentWordIndicesCalculator
import com.dapsoft.wpmcounter.typing.domain.MistakeIndicesCalculator
import com.dapsoft.wpmcounter.typing.domain.SampleTextRepository
import com.dapsoft.wpmcounter.typing.presentation.InputState
import com.dapsoft.wpmcounter.typing.ui.TextMarker
import com.dapsoft.wpmcounter.typing.presentation.TypingOneTimeEvent
import com.dapsoft.wpmcounter.typing.presentation.TypingUiIntent
import com.dapsoft.wpmcounter.typing.presentation.TypingUiState
import com.dapsoft.wpmcounter.ui_common.BaseMviViewModel
import com.dapsoft.wpmcounter.user.UserRepository

import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
internal class TypingViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sampleTextRepository: SampleTextRepository,
    private val clearEventsUseCase: ClearEventsUseCase,
    private val trackKeyPressUseCase: TrackKeyPressUseCase,
    private val observeTypingSpeedUseCase: ObserveTypingSpeedUseCase,
    private val currentWordIndicesCalculator: CurrentWordIndicesCalculator,
    private val mistakeIndicesCalculator: MistakeIndicesCalculator,
    private val wordCounter: WordCounter,
    val textMarker: TextMarker,
    val log: Logger
) : BaseMviViewModel<TypingUiState, TypingUiIntent, TypingOneTimeEvent>(
    TypingUiState(
        userName = "",
        sampleText = "",
        currentWordIndices = Pair(0, 0),
        typedText = "",
        mistakeIndices = emptyList(),
        wordsPerMinute = 0.toDouble(),
        inputState = InputState.PAUSED
    )
) {

    init {
        viewModelScope.launch {
            launch {
                userRepository.observeUserName().collect { userName ->
                    if (userName == null) {
                        sendEvent(TypingOneTimeEvent.LeaveScreen)
                    } else {
                        setState {
                            it.copy(userName = userName)
                        }
                    }
                }
            }

            launch {
                sampleTextRepository.observeText().collect { sampleText ->
                    setState {
                        it.copy(
                            sampleText = sampleText,
                            currentWordIndices = currentWordIndicesCalculator.calculate(sampleText, 0)
                        )
                    }
                    observeTypingSpeedUseCase(sampleText).collect { speedState ->
                        when {
                            uiState.value.inputState == InputState.COMPLETED -> {
                                // Do nothing if completed
                            }
                            speedState is TypingSpeedState.Error -> {
                                setState {
                                    it.copy(
                                        inputState = InputState.ERROR
                                    )
                                }
                            }
                            speedState is TypingSpeedState.Active -> {
                                setState {
                                    it.copy(
                                        inputState = InputState.ACTIVE,
                                        wordsPerMinute = speedState.wordsPerMinute
                                    )
                                }
                            }
                            speedState is TypingSpeedState.Paused -> {
                                setState {
                                    it.copy(
                                        inputState = InputState.PAUSED
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override suspend fun reduce(intent: TypingUiIntent) {
        log.d(TAG) { "Processing intent: $intent" }
        when (intent) {
            TypingUiIntent.ChangeUser -> changeUser()
            is TypingUiIntent.ChangeTypedText -> if (intent.text.length > uiState.value.typedText.length) {
                trackKeyPressUseCase(
                    symbol = intent.text.last(),
                    userName = uiState.value.userName
                ).onFailure {
                    log.e(TAG, it) { "Error tracking key press for symbol='${intent.text.last()}'" }
                    setState {
                        it.copy(
                            inputState = InputState.ERROR
                        )
                    }
                }

                val currentWordNumber = wordCounter.count(intent.text) - 1 + if (intent.text.lastOrNull()?.isWhitespace() == true) 1 else 0

                setState {
                    it.copy(
                        typedText = intent.text,
                        mistakeIndices = mistakeIndicesCalculator.calculate(
                            uiState.value.sampleText,
                            intent.text
                        ),
                        currentWordIndices = currentWordIndicesCalculator.calculate(
                            uiState.value.sampleText,
                            currentWordNumber
                        )
                    )
                }

                val sampleTextWordNumber = wordCounter.count(uiState.value.sampleText)

                if (currentWordNumber + 1 > sampleTextWordNumber) {
                    setState {
                        it.copy(
                            inputState = InputState.COMPLETED
                        )
                    }
                }
            }
            TypingUiIntent.Restart -> clearState()
        }
    }

    private suspend fun clearState() {
        val newInputState = if (clearEventsUseCase().isSuccess) InputState.PAUSED else InputState.ERROR
        setState {
            it.copy(
                typedText = "",
                currentWordIndices = Pair(0, 0),
                mistakeIndices = emptyList(),
                wordsPerMinute = 0.toDouble(),
                inputState = newInputState
            )
        }
    }

    private suspend fun changeUser() {
        clearState()
        userRepository.clearUserName()
    }

    companion object {
        private val TAG = TypingViewModel::class.java.simpleName
    }
}
