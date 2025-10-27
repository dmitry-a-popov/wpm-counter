package com.dapsoft.wpmcounter.common

import kotlin.time.Clock

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class SystemTimeProvider : TimeProvider {

    @OptIn(ExperimentalTime::class)
    override fun now(): Instant = Clock.System.now()
}
