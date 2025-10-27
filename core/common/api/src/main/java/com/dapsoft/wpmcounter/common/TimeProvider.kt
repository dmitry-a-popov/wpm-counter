package com.dapsoft.wpmcounter.common

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

interface TimeProvider {

    @OptIn(ExperimentalTime::class)
    fun now(): Instant
}
