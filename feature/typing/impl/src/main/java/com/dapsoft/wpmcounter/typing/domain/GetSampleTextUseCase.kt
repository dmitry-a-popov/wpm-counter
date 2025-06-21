package com.dapsoft.wpmcounter.typing.domain

import kotlinx.coroutines.flow.Flow

internal interface GetSampleTextUseCase {

    suspend operator fun invoke(): Flow<String>
}