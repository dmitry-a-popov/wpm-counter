package com.dapsoft.wpmcounter.user.domain

import com.dapsoft.wpmcounter.logger.Logger
import com.dapsoft.wpmcounter.logger.e
import com.dapsoft.wpmcounter.user.SaveUserNameUseCase
import com.dapsoft.wpmcounter.user.UserRepository

import javax.inject.Inject

import kotlin.coroutines.cancellation.CancellationException

/**
 * Default implementation of [SaveUserNameUseCase].
 * Performs whitespace trimming before delegating to the repository.
 */
internal class SaveUserNameUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val log: Logger
) : SaveUserNameUseCase {

    override suspend fun invoke(userName: String) = runCatching {
        val normalized = userName.trim()
        require(normalized.isNotEmpty()) { "Username must not be blank" }

        userRepository.saveUserName(userName.trim())
    }.onFailure { exception ->
        log.e(TAG, exception) { "Exception during user name saving: userName='$userName'" }
        if (exception is CancellationException) {
            throw exception
        }
    }

    companion object {
        private val TAG = SaveUserNameUseCaseImpl::class.java.simpleName
    }
}
