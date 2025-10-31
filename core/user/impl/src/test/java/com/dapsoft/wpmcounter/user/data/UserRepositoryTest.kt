package com.dapsoft.wpmcounter.user.data

import com.dapsoft.wpmcounter.user.UserRepository

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class UserRepositoryTest {

    private val userDataStoreDataSource: UserDataStoreDataSource = mockk(relaxed = true)

    private val repository: UserRepository = UserRepositoryImpl(userDataStoreDataSource)

    @Test
    fun `observeUserName delegates to dataSource and returns flow`() = runBlocking {
        val flow = MutableStateFlow<String?>(null)
        every { userDataStoreDataSource.observeUserName() } returns flow

        val observedFlow = repository.observeUserName()

        assertNull(observedFlow.first())
        verify(exactly = 1) { userDataStoreDataSource.observeUserName() }

        flow.value = "testUser"

        assertEquals("testUser", observedFlow.first())
    }

    @Test
    fun `saveUserName delegates to dataSource`() = runBlocking {
        coEvery { userDataStoreDataSource.saveUserName("testName") } returns Unit

        repository.saveUserName("testName")

        coVerify(exactly = 1) { userDataStoreDataSource.saveUserName("testName") }
    }

    @Test
    fun `clearUserName delegates to dataSource`() = runBlocking {
        coEvery { userDataStoreDataSource.clearUserName() } returns Unit

        repository.clearUserName()

        coVerify(exactly = 1) { userDataStoreDataSource.clearUserName() }
    }
}
