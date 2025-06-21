package com.dapsoft.wpmcounter.user.data

import com.dapsoft.wpmcounter.user.domain.UserRepository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

internal class UserRepositoryImpl(private val userDataStoreDataSource: UserDataStoreDataSource) : UserRepository {

    override val userNameFlow: Flow<String>
        get() = userDataStoreDataSource.userNameFlow

    //TODO Inject Dispatchers.IO instead of using it directly
    override suspend fun saveUserName(name: String) = withContext(Dispatchers.IO) {
        userDataStoreDataSource.saveUserName(name)
    }
}