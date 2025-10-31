package com.dapsoft.wpmcounter.user.domain

import com.dapsoft.wpmcounter.logger.LogLevel
import com.dapsoft.wpmcounter.logger.Logger
import com.dapsoft.wpmcounter.user.SaveUserNameUseCase
import com.dapsoft.wpmcounter.user.UserRepository

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify

import kotlinx.coroutines.runBlocking

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Test

import kotlin.coroutines.cancellation.CancellationException

class SaveUserNameUseCaseTest {

    private val userRepository: UserRepository = mockk(relaxed = true)
    private val logger: Logger = mockk(relaxed = true)

    private val useCase: SaveUserNameUseCase = SaveUserNameUseCaseImpl(userRepository, logger)

    private val tag = SaveUserNameUseCaseImpl::class.java.simpleName

    @Test
    fun `invoke with valid name trims whitespace saves and returns success`() = runBlocking {
        coEvery { userRepository.saveUserName("testName") } returns Unit

        val result = useCase.invoke("  testName  ")

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { userRepository.saveUserName("testName") }
        verify(exactly = 0) { logger.log(LogLevel.ERROR, any(), any(), any()) }
    }

    @Test
    fun `invoke with name without whitespace saves and returns success`() = runBlocking {
        coEvery { userRepository.saveUserName("testName") } returns Unit

        val result = useCase.invoke("testName")

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { userRepository.saveUserName("testName") }
        verify(exactly = 0) { logger.log(LogLevel.ERROR, any(), any(), any()) }
    }

    @Test
    fun `invoke with blank name returns failure does not save logs error`() = runBlocking {
        val result = useCase.invoke("   ")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        coVerify(exactly = 0) { userRepository.saveUserName(any()) }
        verify(exactly = 1) { logger.log(LogLevel.ERROR, tag, any(), any()) }
    }

    @Test
    fun `invoke with empty name returns failure does not save logs error`() = runBlocking {
        val result = useCase.invoke("")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        coVerify(exactly = 0) { userRepository.saveUserName(any()) }
        verify(exactly = 1) { logger.log(LogLevel.ERROR, tag, any(), any()) }
    }

    @Test
    fun `invoke repository failure returns failure logs error`() = runBlocking {
        val exception = IllegalStateException("repository fail")
        coEvery { userRepository.saveUserName("testName") } throws exception

        val result = useCase.invoke("  testName  ")

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        coVerify(exactly = 1) { userRepository.saveUserName("testName") }
        verify(exactly = 1) { logger.log(LogLevel.ERROR, tag, exception, any()) }
    }

    @Test
    fun `invoke cancellation rethrows does not swallow cancellation`() = runBlocking {
        val exception = CancellationException("cancel")
        coEvery { userRepository.saveUserName("testName") } throws exception

        var thrown: Throwable? = null
        try {
            useCase.invoke("testName")
        } catch (t: Throwable) {
            thrown = t
        }

        assertEquals(exception, thrown)
        coVerify(exactly = 1) { userRepository.saveUserName("testName") }
        verify(exactly = 1) { logger.log(LogLevel.ERROR, tag, exception, any()) }
    }

    @Test
    fun `invoke failure does not swallow exception type`() = runBlocking {
        val exception = UnsupportedOperationException("unsupported")
        coEvery { userRepository.saveUserName("testName") } throws exception

        val result = useCase.invoke("testName")

        assertFalse(result.isSuccess)
        assertTrue(result.exceptionOrNull() is UnsupportedOperationException)
        verify(exactly = 1) { logger.log(LogLevel.ERROR, tag, exception, any()) }
    }
}
