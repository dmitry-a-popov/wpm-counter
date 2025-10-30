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
import com.dapsoft.wpmcounter.typing.domain.SampleTextProvider
import com.dapsoft.wpmcounter.typing.ui.TextMarker
import com.dapsoft.wpmcounter.ui_common.BaseMviViewModel
import com.dapsoft.wpmcounter.user.UserRepository

import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
internal class TypingViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sampleTextProvider: SampleTextProvider,
    private val clearEventsUseCase: ClearEventsUseCase,
    private val trackKeyPressUseCase: TrackKeyPressUseCase,
    private val observeTypingSpeedUseCase: ObserveTypingSpeedUseCase,
    private val currentWordIndicesCalculator: CurrentWordIndicesCalculator,
    private val mistakeIndicesCalculator: MistakeIndicesCalculator,
    private val wordCounter: WordCounter,
    val textMarker: TextMarker,
    val log: Logger
) : BaseMviViewModel<TypingUiState, TypingIntent, TypingEffect>(TypingUiState.initial()) {

    init {
        viewModelScope.launch {
            launch {
                userRepository.observeUserName().collect { userName ->
                    if (userName == null) {
                        sendSideEffect(TypingEffect.LeaveScreen)
                    } else {
                        setState {
                            it.copy(userName = userName)
                        }
                    }
                }
            }

            launch {
                val sampleText: String = sampleTextProvider.sampleText
                setState {
                    it.copy(
                        sampleText = sampleText,
                        currentWordRange = currentWordIndicesCalculator.calculate(sampleText, 0)
                    )
                }
                observeTypingSpeedUseCase(sampleText).collect { speedState ->
                    when {
                        uiState.value.inputState == TypingInputState.COMPLETED -> {
                            // Do nothing if completed
                        }
                        speedState is TypingSpeedState.Error -> {
                            setState {
                                it.copy(
                                    inputState = TypingInputState.ERROR
                                )
                            }
                        }
                        speedState is TypingSpeedState.Active -> {
                            setState {
                                it.copy(
                                    inputState = TypingInputState.ACTIVE,
                                    wordsPerMinute = speedState.wordsPerMinute
                                )
                            }
                        }
                        speedState is TypingSpeedState.Paused -> {
                            setState {
                                it.copy(
                                    inputState = TypingInputState.PAUSED
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override suspend fun reduce(intent: TypingIntent) {
        log.d(TAG) { "Processing intent: $intent" }
        when (intent) {
            TypingIntent.ChangeUser -> changeUser()
            is TypingIntent.ChangeTypedText -> if (intent.text.length > uiState.value.typedText.length) {
                trackKeyPressUseCase(
                    symbol = intent.text.last(),
                    userName = uiState.value.userName
                ).onFailure { exception ->
                    log.e(TAG, exception) { "Error tracking key press for symbol='${intent.text.last()}'" }
                    setState {
                        it.copy(
                            inputState = TypingInputState.ERROR
                        )
                    }
                }

                val currentWordNumber = wordCounter.count(intent.text) - 1 + if (intent.text.lastOrNull()?.isWhitespace() == true) 1 else 0

                setState {
                    it.copy(
                        typedText = intent.text,
                        mistakeRanges = mistakeIndicesCalculator.calculate(
                            uiState.value.sampleText,
                            intent.text
                        ),
                        currentWordRange = currentWordIndicesCalculator.calculate(
                            uiState.value.sampleText,
                            currentWordNumber
                        )
                    )
                }

                val sampleTextWordNumber = wordCounter.count(uiState.value.sampleText)

                if (currentWordNumber + 1 > sampleTextWordNumber) {
                    setState {
                        it.copy(
                            inputState = TypingInputState.COMPLETED
                        )
                    }
                }
            }
            TypingIntent.Restart -> clearState()
        }
    }

    private suspend fun clearState() {
        val newInputState = if (clearEventsUseCase().isSuccess) TypingInputState.PAUSED else TypingInputState.ERROR
        setState {
            it.copy(
                typedText = "",
                currentWordRange = null,
                mistakeRanges = emptyList(),
                wordsPerMinute = 0.toFloat(),
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
