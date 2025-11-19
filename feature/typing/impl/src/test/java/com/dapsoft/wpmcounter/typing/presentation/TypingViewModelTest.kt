package com.dapsoft.wpmcounter.typing.presentation

import app.cash.turbine.test

import com.dapsoft.wpmcounter.analytics.ClearEventsUseCase
import com.dapsoft.wpmcounter.analytics.TrackKeyPressUseCase
import com.dapsoft.wpmcounter.analytics.speed.ObserveTypingSpeedUseCase
import com.dapsoft.wpmcounter.analytics.speed.TypingSpeedState
import com.dapsoft.wpmcounter.common.WordCounter
import com.dapsoft.wpmcounter.logger.Logger
import com.dapsoft.wpmcounter.test_utils.MainDispatcherRule
import com.dapsoft.wpmcounter.typing.domain.CurrentWordIndicesCalculator
import com.dapsoft.wpmcounter.typing.domain.MistakeIndicesCalculator
import com.dapsoft.wpmcounter.typing.domain.SampleTextProvider
import com.dapsoft.wpmcounter.user.UserRepository

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TypingViewModelTest {

    @get:Rule
    val main = MainDispatcherRule()

    private val sampleText = "type fast now"

    private lateinit var userFlow: MutableSharedFlow<String?>
    private lateinit var speedFlow: MutableSharedFlow<TypingSpeedState>

    private lateinit var userRepository: UserRepository
    private lateinit var sampleTextProvider: SampleTextProvider
    private lateinit var clearEventsUseCase: ClearEventsUseCase
    private lateinit var trackKeyPressUseCase: TrackKeyPressUseCase
    private lateinit var observeTypingSpeedUseCase: ObserveTypingSpeedUseCase
    private lateinit var currentWordIndicesCalculator: CurrentWordIndicesCalculator
    private lateinit var mistakeIndicesCalculator: MistakeIndicesCalculator
    private lateinit var wordCounter: WordCounter
    private lateinit var logger: Logger

    @Before
    fun setUp() {
        userFlow = MutableSharedFlow(replay = 1)
        speedFlow = MutableSharedFlow()

        userRepository = mockk()
        sampleTextProvider = mockk()
        clearEventsUseCase = mockk()
        trackKeyPressUseCase = mockk()
        observeTypingSpeedUseCase = mockk()
        currentWordIndicesCalculator = mockk()
        mistakeIndicesCalculator = mockk()
        wordCounter = mockk()
        logger = mockk(relaxed = true)

        every { userRepository.observeUserName() } returns userFlow
        every { sampleTextProvider.sampleText } returns sampleText
        every { observeTypingSpeedUseCase.invoke(any(), any()) } returns speedFlow
        every { currentWordIndicesCalculator.calculate(any(), any()) } answers {
            val index = secondArg<Int>()
            index..index
        }
        every { mistakeIndicesCalculator.calculate(any(), any()) } answers {
            val typed = secondArg<String>()
            if (typed.isEmpty()) emptyList() else listOf(0 until typed.length)
        }
        every { wordCounter.count(any()) } answers { countWords(firstArg()) }

        coEvery { clearEventsUseCase.invoke() } returns Result.success(Unit)
        coEvery { trackKeyPressUseCase.invoke(any(), any()) } returns Result.success(Unit)
        coEvery { userRepository.clearUserName() } returns Unit
    }

    @Test
    fun `initialization loads sample text`() = runTest {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        assertEquals(sampleText, viewModel.uiState.value.sampleText)
        assertEquals(0..0, viewModel.uiState.value.currentWordRange)
    }

    @Test
    fun `typing speed active updates words per minute`() = runTest {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        speedFlow.emit(TypingSpeedState.Active(52f))
        advanceUntilIdle()

        assertEquals(TypingInputState.ACTIVE, viewModel.uiState.value.inputState)
        assertEquals(52f, viewModel.uiState.value.wordsPerMinute, 0f)
    }

    @Test
    fun `typing speed paused switches state to paused`() = runTest {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        speedFlow.emit(TypingSpeedState.Active(10f))
        advanceUntilIdle()

        speedFlow.emit(TypingSpeedState.Paused)
        advanceUntilIdle()

        assertEquals(TypingInputState.PAUSED, viewModel.uiState.value.inputState)
    }

    @Test
    fun `typing speed error switches state to error`() = runTest {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        speedFlow.emit(TypingSpeedState.Error)
        advanceUntilIdle()

        assertEquals(TypingInputState.ERROR, viewModel.uiState.value.inputState)
    }

    @Test
    fun `user name emission updates state`() = runTest {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        userFlow.emit("Mia")
        advanceUntilIdle()

        assertEquals("Mia", viewModel.uiState.value.userName)
    }

    @Test
    fun `null user name emission triggers leave effect`() = runTest {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.sideEffect.test {
            userFlow.emit(null)

            assertEquals(TypingEffect.LeaveScreen, awaitItem())
        }
    }

    @Test
    fun `change typed text updates ui state`() = runTest {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        userFlow.emit("Neo")
        advanceUntilIdle()

        viewModel.dispatch(TypingIntent.ChangeTypedText("ab"))
        advanceUntilIdle()

        assertEquals("ab", viewModel.uiState.value.typedText)
        assertEquals(listOf(0 until 2), viewModel.uiState.value.mistakeRanges)
        assertEquals(0..0, viewModel.uiState.value.currentWordRange)
    }

    @Test
    fun `change typed text tracks analytics`() = runTest {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        userFlow.emit("Neo")
        advanceUntilIdle()

        viewModel.dispatch(TypingIntent.ChangeTypedText("ab"))
        advanceUntilIdle()

        coVerify { trackKeyPressUseCase.invoke('b', "Neo") }
    }

    @Test
    fun `change typed text ignores deletions`() = runTest {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        userFlow.emit("Neo")
        advanceUntilIdle()

        viewModel.dispatch(TypingIntent.ChangeTypedText("abc"))
        advanceUntilIdle()

        viewModel.dispatch(TypingIntent.ChangeTypedText("ab"))
        advanceUntilIdle()

        assertEquals("abc", viewModel.uiState.value.typedText)
        coVerify(exactly = 1) { trackKeyPressUseCase.invoke(any(), any()) }
    }

    @Test
    fun `change typed text switches to error when analytics tracking fails`() = runTest {
        coEvery { trackKeyPressUseCase.invoke(any(), any()) } returns Result.failure(IllegalStateException("fail"))

        val viewModel = buildViewModel()
        advanceUntilIdle()

        userFlow.emit("Neo")
        advanceUntilIdle()

        viewModel.dispatch(TypingIntent.ChangeTypedText("a"))
        advanceUntilIdle()

        assertEquals(TypingInputState.ERROR, viewModel.uiState.value.inputState)
    }

    @Test
    fun `restart clears typing snapshot`() = runTest {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        userFlow.emit("Neo")
        advanceUntilIdle()

        viewModel.dispatch(TypingIntent.ChangeTypedText("abc"))
        advanceUntilIdle()

        viewModel.dispatch(TypingIntent.Restart)
        advanceUntilIdle()

        coVerify { clearEventsUseCase.invoke() }
        assertEquals("", viewModel.uiState.value.typedText)
        assertTrue(viewModel.uiState.value.mistakeRanges.isEmpty())
        assertEquals(0f, viewModel.uiState.value.wordsPerMinute, 0f)
        assertEquals(TypingInputState.PAUSED, viewModel.uiState.value.inputState)
        assertEquals(0..0, viewModel.uiState.value.currentWordRange)
    }

    @Test
    fun `restart switches to error when analytics clearing fails`() = runTest {
        coEvery { clearEventsUseCase.invoke() } returns Result.failure(IllegalStateException("fail"))

        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.dispatch(TypingIntent.Restart)
        advanceUntilIdle()

        assertEquals(TypingInputState.ERROR, viewModel.uiState.value.inputState)
    }

    @Test
    fun `change user clears repository`() = runTest {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.dispatch(TypingIntent.ChangeTypedText("abc"))
        advanceUntilIdle()

        viewModel.dispatch(TypingIntent.ChangeUser)
        advanceUntilIdle()

        coVerify { userRepository.clearUserName() }
    }

    @Test
    fun `change user resets typing state`() = runTest {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.dispatch(TypingIntent.ChangeTypedText("abc"))
        advanceUntilIdle()

        viewModel.dispatch(TypingIntent.ChangeUser)
        advanceUntilIdle()

        assertEquals("", viewModel.uiState.value.typedText)
        assertTrue(viewModel.uiState.value.mistakeRanges.isEmpty())
    }

    @Test
    fun `typing speed updates stop after completion`() = runTest {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        speedFlow.emit(TypingSpeedState.Active(120f))
        advanceUntilIdle()

        viewModel.dispatch(TypingIntent.ChangeTypedText("$sampleText "))
        advanceUntilIdle()

        speedFlow.emit(TypingSpeedState.Active(300f))
        advanceUntilIdle()

        assertEquals(TypingInputState.COMPLETED, viewModel.uiState.value.inputState)
        assertEquals(120f, viewModel.uiState.value.wordsPerMinute, 0f)
    }

    private fun buildViewModel() = TypingViewModel(
        userRepository,
        sampleTextProvider,
        clearEventsUseCase,
        trackKeyPressUseCase,
        observeTypingSpeedUseCase,
        currentWordIndicesCalculator,
        mistakeIndicesCalculator,
        wordCounter,
        logger
    )

    private fun countWords(text: String): Int {
        if (text.isBlank()) return 0
        return text.trim().split(Regex("\\s+")).size
    }
}

