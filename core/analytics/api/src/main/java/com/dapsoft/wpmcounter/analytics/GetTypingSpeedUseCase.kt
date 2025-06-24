package com.dapsoft.wpmcounter.analytics

import kotlinx.coroutines.flow.Flow

interface GetTypingSpeedUseCase {

    operator fun invoke(validator: WordValidator): Flow<Float>
}