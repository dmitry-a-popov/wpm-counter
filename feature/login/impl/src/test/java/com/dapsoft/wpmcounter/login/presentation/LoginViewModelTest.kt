package com.dapsoft.wpmcounter.login.presentation

import com.dapsoft.wpmcounter.logger.Logger
import com.dapsoft.wpmcounter.test_utils.MainDispatcherRule
import com.dapsoft.wpmcounter.user.SaveUserNameUseCase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineStart

import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    @get:Rule
    val main = MainDispatcherRule()

    private lateinit var saveUserNameUseCase: SaveUserNameUseCase
    private lateinit var logger: Logger

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        saveUserNameUseCase = mockk(relaxed = true)
        logger = mockk(relaxed = true)
        viewModel = LoginViewModel(saveUserNameUseCase, logger)
    }

    @Test
    fun `initial state has empty userName`() {
        assertEquals("", viewModel.uiState.value.userName)
    }

    @Test
    fun `onUserNameChanged updates state with new name`() {
        val newName = "testUser"

        viewModel.dispatch(LoginIntent.OnUserNameChanged(newName))

        assertEquals(newName, viewModel.uiState.value.userName)
    }

    @Test
    fun `onUserNameChanged updates state multiple times`() {
        viewModel.dispatch(LoginIntent.OnUserNameChanged("first"))
        assertEquals("first", viewModel.uiState.value.userName)

        viewModel.dispatch(LoginIntent.OnUserNameChanged("second"))
        assertEquals("second", viewModel.uiState.value.userName)
    }

    @Test
    fun `onLoginConfirmed with success emits LeaveScreen effect`() = runBlocking {
        val userName = "testUser"
        coEvery { saveUserNameUseCase(userName) } returns Result.success(Unit)

        val effectDeferred = async(start = CoroutineStart.UNDISPATCHED) {
            withTimeout(1_000) { viewModel.sideEffect.first() }
        }

        viewModel.dispatch(LoginIntent.OnUserNameChanged(userName))
        viewModel.dispatch(LoginIntent.OnLoginConfirmed)

        val effect = effectDeferred.await()
        assertTrue(effect is LoginEffect.LeaveScreen)

        coVerify(exactly = 1) { saveUserNameUseCase(userName) }
    }

    @Test
    fun `onLoginConfirmed with failure emits ShowLoginError effect`() = runBlocking {
        val userName = "testUser"
        val exception = IllegalStateException("save failed")
        coEvery { saveUserNameUseCase(userName) } returns Result.failure(exception)

        val effectDeferred = async(start = CoroutineStart.UNDISPATCHED) {
            withTimeout(1_000) { viewModel.sideEffect.first() }
        }

        viewModel.dispatch(LoginIntent.OnUserNameChanged(userName))
        viewModel.dispatch(LoginIntent.OnLoginConfirmed)

        val effect = effectDeferred.await()
        assertTrue(effect is LoginEffect.ShowLoginError)

        coVerify(exactly = 1) { saveUserNameUseCase(userName) }
    }

    @Test
    fun `onLoginConfirmed uses current state userName`() = runBlocking {
        coEvery { saveUserNameUseCase("updated") } returns Result.success(Unit)

        val effectDeferred = async(start = CoroutineStart.UNDISPATCHED) {
            withTimeout(1_000) { viewModel.sideEffect.first() }
        }

        viewModel.dispatch(LoginIntent.OnUserNameChanged("initial"))
        viewModel.dispatch(LoginIntent.OnUserNameChanged("updated"))
        viewModel.dispatch(LoginIntent.OnLoginConfirmed)

        val effect = effectDeferred.await()
        assertTrue(effect is LoginEffect.LeaveScreen)

        coVerify(exactly = 1) { saveUserNameUseCase("updated") }
    }

    @Test
    fun `onLoginConfirmed logs intent processing`() {
        val userName = "testUser"
        coEvery { saveUserNameUseCase(userName) } returns Result.success(Unit)

        viewModel.dispatch(LoginIntent.OnUserNameChanged(userName))
        viewModel.dispatch(LoginIntent.OnLoginConfirmed)

        verify(exactly = 2) { logger.log(any(), any(), null, any()) }
    }

    @Test
    fun `onUserNameChanged logs intent processing`() {
        viewModel.dispatch(LoginIntent.OnUserNameChanged("test"))

        verify(exactly = 1) { logger.log(any(), any(), null, any()) }
    }
}

