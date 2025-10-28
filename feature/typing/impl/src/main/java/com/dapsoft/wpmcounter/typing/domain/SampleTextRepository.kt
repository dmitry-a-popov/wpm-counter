package com.dapsoft.wpmcounter.typing.domain

import kotlinx.coroutines.flow.Flow

internal interface SampleTextRepository {

    fun observeText(): Flow<String>
}
