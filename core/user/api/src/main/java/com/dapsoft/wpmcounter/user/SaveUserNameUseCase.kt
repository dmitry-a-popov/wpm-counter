package com.dapsoft.wpmcounter.user

/**
 * Use case for persisting the current user name.
 */
interface SaveUserNameUseCase {

    suspend operator fun invoke(userName: String): Result<Unit>
}
