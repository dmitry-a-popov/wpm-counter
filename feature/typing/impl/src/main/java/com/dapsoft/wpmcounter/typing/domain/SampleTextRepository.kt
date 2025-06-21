package com.dapsoft.wpmcounter.typing.domain

import kotlinx.coroutines.flow.Flow

internal interface SampleTextRepository {

    val sampleTextFlow: Flow<String>
}