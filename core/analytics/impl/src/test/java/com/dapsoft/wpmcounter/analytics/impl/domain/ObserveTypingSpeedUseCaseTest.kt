package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.analytics.impl.domain.model.KeystrokeEvent
import com.dapsoft.wpmcounter.analytics.impl.domain.model.SessionState
import com.dapsoft.wpmcounter.analytics.speed.TypingSpeedState
import com.dapsoft.wpmcounter.common.orientation.ScreenOrientation
import com.dapsoft.wpmcounter.common.validation.TextValidator
import com.dapsoft.wpmcounter.common.validation.WordComparison
import com.dapsoft.wpmcounter.logger.LogLevel
import com.dapsoft.wpmcounter.logger.Logger

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertThrows
import org.junit.Test

import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class ObserveTypingSpeedUseCaseTest {

    private val analyticsRepo: BehavioralAnalyticsRepository = mockk(relaxed = true)
    private val speedCalculator: SpeedCalculator = mockk(relaxed = true)
    private val sessionUpdater: TypingSessionUpdater = mockk(relaxed = true)
    private val textValidator: TextValidator = mockk(relaxed = true)
    private val logger: Logger = mockk(relaxed = true)

    private val useCase = ObserveTypingSpeedUseCaseImpl(analyticsRepo, speedCalculator, sessionUpdater, textValidator, logger)

    @Test
    fun `flow emits Active then Paused for single event`() = runTest {
        val pause = 40.milliseconds
        val eventTime = Instant.fromEpochMilliseconds(1_000L)
        val event = KeystrokeEvent(eventTime, 'a', ScreenOrientation.PORTRAIT, "u")
        val upstream = MutableSharedFlow<KeystrokeEvent?>(replay = 1)
        every { analyticsRepo.observeLatestEvent() } returns upstream
        every { sessionUpdater.updateForKeystroke('a', eventTime, pause) } returns SessionState(
            lastEventTimestamp = eventTime,
            totalActiveTypingTime = 1_000.milliseconds,
            currentText = "hello"
        )
        every { textValidator.compareWords("hello world", "hello") } returns listOf(WordComparison(true))
        every { speedCalculator.calculateWordsPerMinute(1, 1_000.milliseconds) } returns 60f

        val states = mutableListOf<TypingSpeedState>()
        val job = launch { useCase.invoke("hello world", pause).take(2).toList(states) }
        upstream.emit(event)
        withTimeout(600) { job.join() }

        assertEquals(2, states.size)
        assertTrue(states[0] is TypingSpeedState.Active)
        assertEquals(60f, (states[0] as TypingSpeedState.Active).wordsPerMinute)
        assertEquals(TypingSpeedState.Paused, states[1])
    }

    @Test
    fun `distinctUntilChanged filters identical consecutive Active states`() = runTest {
        val pause = 80.milliseconds
        val upstream = MutableSharedFlow<KeystrokeEvent?>(replay = 1)
        every { analyticsRepo.observeLatestEvent() } returns upstream
        val state = SessionState(lastEventTimestamp = Instant.fromEpochMilliseconds(10), totalActiveTypingTime = 500.milliseconds, currentText = "ok")
        every { sessionUpdater.updateForKeystroke(any(), any(), pause) } returns state
        every { textValidator.compareWords(any(), any()) } returns listOf(WordComparison(true))
        every { speedCalculator.calculateWordsPerMinute(1, 500.milliseconds) } returns 50f

        val states = mutableListOf<TypingSpeedState>()
        val job = launch { useCase.invoke("sample", pause).take(2).toList(states) }
        upstream.emit(KeystrokeEvent(Instant.fromEpochMilliseconds(10), 'x', ScreenOrientation.PORTRAIT, "u"))
        upstream.emit(KeystrokeEvent(Instant.fromEpochMilliseconds(15), 'y', ScreenOrientation.PORTRAIT, "u"))
        withTimeout(700) { job.join() }

        assertEquals(listOf(TypingSpeedState.Active(50f), TypingSpeedState.Paused), states)
    }

    @Test
    fun `flow emits Error after non cancellation exception and continues`() = runTest {
        val pause = 50.milliseconds
        val exception = RuntimeException("boom")
        every { analyticsRepo.observeLatestEvent() } returns flow {
            val t = Instant.fromEpochMilliseconds(1)
            emit(KeystrokeEvent(t, 'a', ScreenOrientation.PORTRAIT, "u"))
            throw exception
        }
        every { sessionUpdater.updateForKeystroke('a', any(), pause) } returns SessionState(
            lastEventTimestamp = Instant.fromEpochMilliseconds(1),
            totalActiveTypingTime = 200.milliseconds,
            currentText = "a"
        )
        every { textValidator.compareWords("a", "a") } returns listOf(WordComparison(true))
        every { speedCalculator.calculateWordsPerMinute(1, 200.milliseconds) } returns 30f

        val states = mutableListOf<TypingSpeedState>()
        withTimeout(600) { useCase.invoke("a", pause).take(2).toList(states) }

        assertTrue(states[0] is TypingSpeedState.Active)
        assertEquals(30f, (states[0] as TypingSpeedState.Active).wordsPerMinute)
        assertEquals(TypingSpeedState.Error, states[1])
        verify(exactly = 1) { logger.log(eq(LogLevel.ERROR), any(), match { it.message == exception.message }, any()) }
    }

    @Test
    fun `flow rethrows cancellation exception`() = runTest {
        val pause = 30.milliseconds
        every { analyticsRepo.observeLatestEvent() } returns flow {
            throw CancellationException("cancel")
        }

        assertThrows(CancellationException::class.java) {
            runBlocking { useCase.invoke("x", pause).take(1).toList() }
        }
        verify(exactly = 0) { logger.log(LogLevel.ERROR, any(), any(), any()) }
    }

    @Test
    fun `invoke requires positive pause threshold`() {
        assertThrows(IllegalArgumentException::class.java) {
            useCase.invoke("text", Duration.ZERO)
        }
    }

    @Test
    fun `active speed calculation uses validator matches count`() = runTest {
        val pause = 40.milliseconds
        val upstream = MutableSharedFlow<KeystrokeEvent?>(replay = 1)
        every { analyticsRepo.observeLatestEvent() } returns upstream
        val sessionState = SessionState(
            lastEventTimestamp = Instant.fromEpochMilliseconds(100),
            totalActiveTypingTime = 1_500.milliseconds,
            currentText = "one two three"
        )
        every { sessionUpdater.updateForKeystroke(any(), any(), pause) } returns sessionState
        every { textValidator.compareWords("one two three", sessionState.currentText) } returns listOf(
            WordComparison(true), WordComparison(true), WordComparison(true)
        )
        every { speedCalculator.calculateWordsPerMinute(3, 1_500.milliseconds) } returns 120f

        val states = mutableListOf<TypingSpeedState>()
        val job = launch { useCase.invoke("one two three", pause).take(1).toList(states) }
        upstream.emit(KeystrokeEvent(Instant.fromEpochMilliseconds(100), 'x', ScreenOrientation.PORTRAIT, "u"))
        withTimeout(400) { job.join() }

        assertEquals(listOf(TypingSpeedState.Active(120f)), states)
    }
}
