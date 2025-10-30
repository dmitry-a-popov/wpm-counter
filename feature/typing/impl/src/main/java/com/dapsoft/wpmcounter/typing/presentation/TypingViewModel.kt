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
import com.dapsoft.wpmcounter.ui_common.BaseMviViewModel
import com.dapsoft.wpmcounter.user.UserRepository

import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.launch

import javax.inject.Inject

/**
 * ViewModel for the typing feature (lightweight MVI).
 *
 * Responsibilities:
 * - Provides immutable [TypingUiState] to the UI.
 * - Consumes serialized [TypingIntent] instances and applies state transitions.
 * - Observes:
 *   - Current user name (navigation away when cleared).
 *   - Typing speed stream (active / paused / error / completed).
 * - Tracks key presses (append-only input; deletions intentionally ignored).
 * - Calculates:
 *   - Mistake ranges.
 *   - Current word character range.
 * - Emits one-time [TypingEffect] (currently only navigation).
 *
 * Design notes:
 * - Append-only model: if user deletes characters they are ignored; state stays at last length.
 * - [computeCurrentWordNumber] interprets trailing whitespace as starting a new word.
 * - Completion is detected when computed current word number >= [sampleTextWordCount].
 * - Word ranges & mistake ranges use inclusive [IntRange] internally; domain calculators expected to supply consistent semantics.
 */
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
    val log: Logger
) : BaseMviViewModel<TypingUiState, TypingIntent, TypingEffect>(TypingUiState.initial()) {

    /**
     * Cached number of words in the sample text to avoid recomputation per keystroke.
     */
    private var sampleTextWordCount: Int = 0

    init {
        observeUser()
        initializeSampleTextAndSpeed()
    }

    /**
     * Observes the current user name; emits navigation side effect if cleared.
     */
    private fun observeUser() = viewModelScope.launch {
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

    /**
     * Initializes sample text (state + precomputed word count) and starts collecting typing speed.
     * Stops applying speed updates once input state is `COMPLETED`.
     */
    private fun initializeSampleTextAndSpeed() = viewModelScope.launch {
        val sampleText = sampleTextProvider.sampleText
        sampleTextWordCount = wordCounter.count(sampleText)
        setState {
            it.copy(
                sampleText = sampleText,
                currentWordRange = currentWordIndicesCalculator.calculate(sampleText, 0)
            )
        }
        observeTypingSpeedUseCase(sampleText).collect { speedState ->
            val current = uiState.value
            if (current.inputState == TypingInputState.COMPLETED) {
                return@collect
            }
            when (speedState) {
                is TypingSpeedState.Error -> {
                    setState { it.copy(inputState = TypingInputState.ERROR) }
                }
                is TypingSpeedState.Active -> {
                    setState {
                        it.copy(
                            inputState = TypingInputState.ACTIVE,
                            wordsPerMinute = speedState.wordsPerMinute
                        )
                    }
                }
                is TypingSpeedState.Paused -> {
                    setState { it.copy(inputState = TypingInputState.PAUSED) }
                }
            }
        }
    }

    /**
     * Intent reducer (single-threaded). Delegates to intent-specific handlers.
     */
    override suspend fun reduce(intent: TypingIntent) {
        log.d(TAG) { "Processing intent: $intent" }
        when (intent) {
            TypingIntent.ChangeUser -> changeUser()
            TypingIntent.Restart -> restart()
            is TypingIntent.ChangeTypedText -> handleTextAppended(intent.text)
        }
    }

    /**
     * Handles appended text only.
     * - Ignores if input length decreased or stayed the same (deletions intentionally unsupported).
     * - Tracks last appended character.
     * - Recomputes mistake ranges + current word range.
     * - Marks completion if word count threshold reached.
     */
    private suspend fun handleTextAppended(newText: String) {
        val oldText = uiState.value.typedText
        if (newText.length <= oldText.length) return

        val addedChar = newText.last()
        trackKeyPressUseCase(symbol = addedChar, userName = uiState.value.userName)
            .onFailure { ex ->
                log.e(TAG, ex) { "Error tracking key press '$addedChar'" }
                setState { it.copy(inputState = TypingInputState.ERROR) }
            }

        val currentWordNumber = computeCurrentWordNumber(newText)
        val sampleText = uiState.value.sampleText

        setState {
            it.copy(
                typedText = newText,
                mistakeRanges = mistakeIndicesCalculator.calculate(sampleText, newText),
                currentWordRange = currentWordIndicesCalculator.calculate(sampleText, currentWordNumber)
            )
        }

        if (currentWordNumber >= sampleTextWordCount) {
            setState { it.copy(inputState = TypingInputState.COMPLETED) }
        }
    }

    /**
     * Derives zero-based current word number.
     * - Counts completed words ([wordCounter.count()]).
     * - If last char is whitespace, user started a new (empty) word -> return base count.
     * - Else still typing current word -> return `base - 1`.
     */
    private fun computeCurrentWordNumber(text: String): Int {
        if (text.isEmpty()) return 0
        val base = wordCounter.count(text)
        return if (text.last().isWhitespace()) base else base - 1
    }

    /**
     * Clears analytics events and resets typing state to initial (keeping loaded sample text & user).
     * Sets `PAUSED` or `ERROR` depending on clear outcome.
     */
    private suspend fun restart() {
        val result = clearEventsUseCase()
        val newInputState = if (result.isSuccess) TypingInputState.PAUSED else TypingInputState.ERROR
        setState {
            it.copy(
                typedText = "",
                mistakeRanges = emptyList(),
                wordsPerMinute = 0.toFloat(),
                inputState = newInputState,
                currentWordRange = currentWordIndicesCalculator.calculate(it.sampleText, 0)
            )
        }
    }

    /**
     * Changes user:
     * - Resets typing state.
     * - Clears stored user name (triggers navigation via [observeUser]).
     */
    private suspend fun changeUser() {
        restart()
        userRepository.clearUserName()
    }

    companion object {
        private val TAG = TypingViewModel::class.java.simpleName
    }
}
