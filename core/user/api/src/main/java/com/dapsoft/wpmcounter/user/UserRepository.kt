package com.dapsoft.wpmcounter.user

import kotlinx.coroutines.flow.Flow

/**
 * Repository exposing the current user name.
 *
 * Responsibilities:
 * - Provide a reactive stream of the stored user name or null if nothing stored.
 * - Persist a new user name.
 * - Clear the user name explicitly.
 */
interface UserRepository {
    fun observeUserName(): Flow<String?>
    suspend fun saveUserName(name: String)
    suspend fun clearUserName()
}
