package com.dapsoft.wpmcounter.common

import android.os.SystemClock

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class SystemTimeProvider : TimeProvider {

    override fun getElapsedRealtime(): Duration = SystemClock.elapsedRealtime().milliseconds
}