package com.dapsoft.wpmcounter.user

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val userNameFlow: Flow<String?>
    suspend fun saveUserName(name: String)
}