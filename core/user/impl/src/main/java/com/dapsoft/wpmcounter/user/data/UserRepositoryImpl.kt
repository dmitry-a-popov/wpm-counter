package com.dapsoft.wpmcounter.user.data

import com.dapsoft.wpmcounter.user.UserRepository

import kotlinx.coroutines.flow.Flow

internal class UserRepositoryImpl(private val userDataStoreDataSource: UserDataStoreDataSource) : UserRepository {

    override val name: Flow<String>
        get() = userDataStoreDataSource.userName

    override suspend fun saveUserName(name: String) {
        userDataStoreDataSource.saveUserName(name)
    }
}
