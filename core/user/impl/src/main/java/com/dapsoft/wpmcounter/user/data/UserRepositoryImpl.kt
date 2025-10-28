package com.dapsoft.wpmcounter.user.data

import com.dapsoft.wpmcounter.user.UserRepository

import kotlinx.coroutines.flow.Flow

/**
 * DataStore-backed implementation of [UserRepository].
 */
internal class UserRepositoryImpl(private val userDataStoreDataSource: UserDataStoreDataSource) : UserRepository {

    override fun observeUserName(): Flow<String> = userDataStoreDataSource.observeUserName()

    override suspend fun saveUserName(name: String) {
        userDataStoreDataSource.saveUserName(name)
    }

    override suspend fun clearUserName() {
        userDataStoreDataSource.saveUserName("")
    }
}
