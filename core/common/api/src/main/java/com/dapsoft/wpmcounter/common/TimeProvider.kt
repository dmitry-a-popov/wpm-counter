package com.dapsoft.wpmcounter.common

import kotlin.time.Duration

interface TimeProvider {

    fun getElapsedRealtime(): Duration
}