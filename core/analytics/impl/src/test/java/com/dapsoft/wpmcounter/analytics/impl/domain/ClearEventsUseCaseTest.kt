package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.analytics.impl.domain.model.SessionState
import com.dapsoft.wpmcounter.logger.LogLevel
import com.dapsoft.wpmcounter.logger.Logger

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

import kotlinx.coroutines.runBlocking

import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Assert.assertEquals
import org.junit.Test

import kotlin.coroutines.cancellation.CancellationException

class ClearEventsUseCaseTest {

    private val repository: BehavioralAnalyticsRepository = mockk(relaxed = true)
    private val stateStore: TypingSessionStateStore = mockk(relaxed = true) {
        every { state } returns SessionState.initial()
    }
    private val logger: Logger = mockk(relaxed = true)

    private val useCase = ClearEventsUseCaseImpl(repository, stateStore, logger)

    private val tag = ClearEventsUseCaseImpl::class.java.simpleName

    @Test
    fun `invoke success deletes events resets state and returns success`() = runBlocking {
        coEvery { repository.deleteAllEvents() } returns Unit
        every { stateStore.reset() } returns Unit

        val result = useCase.invoke()

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { repository.deleteAllEvents() }
        verify(exactly = 1) { stateStore.reset() }
        verify(exactly = 0) { logger.log(LogLevel.ERROR, any(), any(), any()) }
    }

    @Test
    fun `invoke repository failure returns failure does not reset state logs error`() = runBlocking {
        val exception = IllegalStateException("fail")
        coEvery { repository.deleteAllEvents() } throws exception

        val result = useCase.invoke()

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        coVerify(exactly = 1) { repository.deleteAllEvents() }
        verify(exactly = 0) { stateStore.reset() }
        verify(exactly = 1) { logger.log(LogLevel.ERROR, tag, exception, any()) }
    }

    @Test
    fun `invoke cancellation rethrows does not reset state logs error`() = runBlocking {
        val exception = CancellationException("cancel")
        coEvery { repository.deleteAllEvents() } throws exception

        var thrown: Throwable? = null
        try {
            useCase.invoke()
        } catch (t: Throwable) {
            thrown = t
        }

        assertEquals(exception, thrown)
        coVerify(exactly = 1) { repository.deleteAllEvents() }
        verify(exactly = 0) { stateStore.reset() }
        verify(exactly = 1) { logger.log(LogLevel.ERROR, tag, exception, any()) }
    }

    @Test
    fun `invoke reset failure returns failure logs error after successful delete`() = runBlocking {
        coEvery { repository.deleteAllEvents() } returns Unit
        val resetException = RuntimeException("reset fail")
        every { stateStore.reset() } throws resetException

        val result = useCase.invoke()

        assertTrue(result.isFailure)
        assertEquals(resetException, result.exceptionOrNull())
        coVerify(exactly = 1) { repository.deleteAllEvents() }
        verify(exactly = 1) { stateStore.reset() }
        verify(exactly = 1) { logger.log(LogLevel.ERROR, tag, resetException, any()) }
    }

    @Test
    fun `invoke success does not log error`() = runBlocking {
        coEvery { repository.deleteAllEvents() } returns Unit
        every { stateStore.reset() } returns Unit

        useCase.invoke()

        verify(exactly = 0) { logger.log(LogLevel.ERROR, any(), any(), any()) }
    }

    @Test
    fun `invoke failure does not swallow exception types`() = runBlocking {
        val exception = UnsupportedOperationException("uoe")
        coEvery { repository.deleteAllEvents() } throws exception

        val result = useCase.invoke()

        assertTrue(result.exceptionOrNull() is UnsupportedOperationException)
        assertFalse(result.isSuccess)
    }
}

