package com.dapsoft.wpmcounter.user.data

import com.dapsoft.wpmcounter.user.UserRepository

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

import javax.inject.Singleton

/**
 * DataStore-backed implementation of [UserRepository].
 */
@Singleton
internal class UserRepositoryImpl @Inject constructor(
    private val userDataStoreDataSource: UserDataStoreDataSource
) : UserRepository {

    override fun observeUserName(): Flow<String?> = userDataStoreDataSource.observeUserName()

    override suspend fun saveUserName(name: String) {
        userDataStoreDataSource.saveUserName(name)
    }

    override suspend fun clearUserName() {
        userDataStoreDataSource.clearUserName()
    }
}
