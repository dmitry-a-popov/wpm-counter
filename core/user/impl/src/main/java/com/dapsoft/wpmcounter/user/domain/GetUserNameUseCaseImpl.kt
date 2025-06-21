package com.dapsoft.wpmcounter.user.domain

import com.dapsoft.wpmcounter.user.GetUserNameUseCase

internal class GetUserNameUseCaseImpl(private val userRepository: UserRepository) : GetUserNameUseCase {

    override fun invoke() = userRepository.userNameFlow
}