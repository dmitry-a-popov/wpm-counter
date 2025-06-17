package com.dapsoft.wpmcounter.user

import kotlinx.coroutines.flow.Flow

internal class UserRepositoryImpl(private val userDataStoreDataSource: UserDataStoreDataSource) : UserRepository {

    override val userNameFlow: Flow<String?>
        get() = userDataStoreDataSource.userNameFlow

    override suspend fun saveUserName(name: String) {
        userDataStoreDataSource.saveUserName(name)
    }
}