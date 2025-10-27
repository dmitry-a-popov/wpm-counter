package com.dapsoft.wpmcounter.common

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Provides the current wall clock time
 */
interface TimeProvider {

    @OptIn(ExperimentalTime::class)
    fun now(): Instant
}
