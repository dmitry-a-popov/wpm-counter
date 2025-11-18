package com.dapsoft.wpmcounter.login.presentation

import app.cash.turbine.test

import com.dapsoft.wpmcounter.logger.LogLevel
import com.dapsoft.wpmcounter.logger.Logger
import com.dapsoft.wpmcounter.test_utils.MainDispatcherRule
import com.dapsoft.wpmcounter.user.SaveUserNameUseCase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify

import kotlinx.coroutines.test.runTest

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
    fun `onLoginConfirmed with success emits LeaveScreen effect`() = runTest {
        val userName = "testUser"
        coEvery { saveUserNameUseCase(userName) } returns Result.success(Unit)

        viewModel.sideEffect.test {
            viewModel.dispatch(LoginIntent.OnUserNameChanged(userName))
            viewModel.dispatch(LoginIntent.OnLoginConfirmed)

            assertTrue(awaitItem() is LoginEffect.LeaveScreen)
        }

        coVerify(exactly = 1) { saveUserNameUseCase(userName) }
    }

    @Test
    fun `onLoginConfirmed with failure emits ShowLoginError effect`() = runTest {
        val userName = "testUser"
        val exception = IllegalStateException("save failed")
        coEvery { saveUserNameUseCase(userName) } returns Result.failure(exception)

        viewModel.sideEffect.test {
            viewModel.dispatch(LoginIntent.OnUserNameChanged(userName))
            viewModel.dispatch(LoginIntent.OnLoginConfirmed)

            assertTrue(awaitItem() is LoginEffect.ShowLoginError)
        }

        coVerify(exactly = 1) { saveUserNameUseCase(userName) }
    }

    @Test
    fun `onLoginConfirmed uses current state userName`() = runTest {
        coEvery { saveUserNameUseCase("updated") } returns Result.success(Unit)

        viewModel.sideEffect.test {
            viewModel.dispatch(LoginIntent.OnUserNameChanged("initial"))
            viewModel.dispatch(LoginIntent.OnUserNameChanged("updated"))
            viewModel.dispatch(LoginIntent.OnLoginConfirmed)

            assertTrue(awaitItem() is LoginEffect.LeaveScreen)
        }

        coVerify(exactly = 1) { saveUserNameUseCase("updated") }
    }

    @Test
    fun `onLoginConfirmed logs intent processing`() {
        val userName = "testUser"
        coEvery { saveUserNameUseCase(userName) } returns Result.success(Unit)

        val loginConfirmedIntent = LoginIntent.OnLoginConfirmed

        viewModel.dispatch(LoginIntent.OnUserNameChanged(userName))
        viewModel.dispatch(loginConfirmedIntent)

        verify(exactly = 1) {
            logger.log(
                eq(LogLevel.DEBUG),
                eq(LoginViewModel.TAG),
                isNull(),
                match<() -> String> { lambda ->
                    lambda().contains("$loginConfirmedIntent")
                }
            )
        }
    }

    @Test
    fun `onUserNameChanged logs intent processing`() {
        val intent = LoginIntent.OnUserNameChanged("test")
        viewModel.dispatch(intent)

        verify(exactly = 1) {
            logger.log(
                eq(LogLevel.DEBUG),
                eq(LoginViewModel.TAG),
                isNull(),
                match<() -> String> { lambda ->
                    lambda().contains("$intent")
                }
            )
        }
    }
}

