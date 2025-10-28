package com.dapsoft.wpmcounter.user.domain

import com.dapsoft.wpmcounter.user.SaveUserNameUseCase
import com.dapsoft.wpmcounter.user.UserRepository

/**
 * Default implementation of [SaveUserNameUseCase].
 * Performs whitespace trimming before delegating to the repository.
 */
internal class SaveUserNameUseCaseImpl(private val userRepository: UserRepository) : SaveUserNameUseCase {

    override suspend fun invoke(userName: String) {
        val normalized = userName.trim()
        require(normalized.isNotEmpty()) { "Username must not be blank" }

        userRepository.saveUserName(userName.trim())
    }
}
