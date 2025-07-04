package com.dapsoft.wpmcounter.user.domain

import com.dapsoft.wpmcounter.user.SaveUserNameUseCase

internal class SaveUserNameUseCaseImpl(private val userRepository: UserRepository) : SaveUserNameUseCase {

    override suspend fun invoke(userName: String) {
        userRepository.saveUserName(userName)
    }
}