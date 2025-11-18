package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.analytics.TrackKeyPressUseCase
import com.dapsoft.wpmcounter.common.TimeProvider
import com.dapsoft.wpmcounter.common.orientation.ScreenOrientation
import com.dapsoft.wpmcounter.common.orientation.ScreenOrientationProvider
import com.dapsoft.wpmcounter.logger.LogLevel
import com.dapsoft.wpmcounter.logger.Logger

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Test

import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class TrackKeyPressUseCaseTest {

    private val repository: BehavioralAnalyticsRepository = mockk(relaxed = true)
    private val orientationProvider: ScreenOrientationProvider = mockk(relaxed = true)
    private val timeProvider: TimeProvider = mockk(relaxed = true)
    private val logger: Logger = mockk(relaxed = true)

    private val useCase: TrackKeyPressUseCase = TrackKeyPressUseCaseImpl(repository, orientationProvider, timeProvider, logger)

    private val tag = TrackKeyPressUseCaseImpl::class.java.simpleName

    @Test
    fun `successful invoke saves event with correct fields`() = runTest {
        val now = Instant.fromEpochMilliseconds(1_234L)
        every { timeProvider.now() } returns now
        every { orientationProvider.currentOrientation } returns ScreenOrientation.LANDSCAPE
        coEvery { repository.saveEvent(any()) } returns Unit
        val captured = slot<com.dapsoft.wpmcounter.analytics.impl.domain.model.KeystrokeEvent>()

        val result = useCase.invoke('X', "user1")

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { repository.saveEvent(capture(captured)) }
        assertEquals(now.toEpochMilliseconds(), captured.captured.eventTime.toEpochMilliseconds())
        assertEquals('X', captured.captured.symbol)
        assertEquals(ScreenOrientation.LANDSCAPE, captured.captured.screenOrientation)
        assertEquals("user1", captured.captured.userName)
        verify(exactly = 0) { logger.log(LogLevel.ERROR, tag, any(), any()) }
    }

    @Test
    fun `blank userName returns failure does not save event logs error`() = runTest {
        every { timeProvider.now() } returns Instant.fromEpochMilliseconds(10)
        every { orientationProvider.currentOrientation } returns ScreenOrientation.PORTRAIT

        val result = useCase.invoke('a', "   ")

        assertTrue(result.isFailure)
        coVerify(exactly = 0) { repository.saveEvent(any()) }
        verify(exactly = 1) { logger.log(eq(LogLevel.ERROR), eq(tag), any(), any()) }
    }

    @Test
    fun `repository failure returns failure logs error`() = runTest {
        val now = Instant.fromEpochMilliseconds(99)
        every { timeProvider.now() } returns now
        every { orientationProvider.currentOrientation } returns ScreenOrientation.PORTRAIT
        val ex = IllegalStateException("db fail")
        coEvery { repository.saveEvent(any()) } throws ex

        val result = useCase.invoke('#', "u")

        assertTrue(result.isFailure)
        assertEquals(ex, result.exceptionOrNull())
        verify(exactly = 1) { logger.log(LogLevel.ERROR, eq(tag), eq(ex), any()) }
    }

    @Test
    fun `cancellation rethrows`() = runTest {
        val now = Instant.fromEpochMilliseconds(5)
        every { timeProvider.now() } returns now
        every { orientationProvider.currentOrientation } returns ScreenOrientation.UNDEFINED
        coEvery { repository.saveEvent(any()) } throws CancellationException("cancel")

        assertThrows(CancellationException::class.java) {
            runBlocking { useCase.invoke('z', "user") }
        }
        verify(exactly = 1) { logger.log(eq(LogLevel.ERROR), eq(tag), any(), any()) }
    }

    @Test
    fun `failure path does not swallow exception type`() = runTest {
        val now = Instant.fromEpochMilliseconds(7)
        every { timeProvider.now() } returns now
        every { orientationProvider.currentOrientation } returns ScreenOrientation.PORTRAIT
        val ex = UnsupportedOperationException("oops")
        coEvery { repository.saveEvent(any()) } throws ex

        val result = useCase.invoke('t', "user")

        assertFalse(result.isSuccess)
        assertTrue(result.exceptionOrNull() is UnsupportedOperationException)
    }
}

