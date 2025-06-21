package com.dapsoft.wpmcounter.user

import kotlinx.coroutines.flow.Flow

interface GetUserNameUseCase {

    operator fun invoke(): Flow<String>
}