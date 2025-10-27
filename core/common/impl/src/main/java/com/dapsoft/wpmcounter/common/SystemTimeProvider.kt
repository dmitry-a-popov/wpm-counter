package com.dapsoft.wpmcounter.common

import javax.inject.Inject

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * System implementation using wall clock time.
 */
internal class SystemTimeProvider @Inject constructor() : TimeProvider {

    @OptIn(ExperimentalTime::class)
    override fun now(): Instant = Clock.System.now()
}
