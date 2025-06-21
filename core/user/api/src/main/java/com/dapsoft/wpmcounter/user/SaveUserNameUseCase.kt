package com.dapsoft.wpmcounter.user

interface SaveUserNameUseCase {

    suspend operator fun invoke(userName: String)
}