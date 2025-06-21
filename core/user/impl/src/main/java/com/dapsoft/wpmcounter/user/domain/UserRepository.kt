package com.dapsoft.wpmcounter.user.domain

import kotlinx.coroutines.flow.Flow

internal interface UserRepository {
    val userNameFlow: Flow<String>
    suspend fun saveUserName(name: String)
}