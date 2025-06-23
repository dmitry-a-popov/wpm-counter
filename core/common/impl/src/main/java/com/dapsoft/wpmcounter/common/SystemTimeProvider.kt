package com.dapsoft.wpmcounter.common

import android.os.SystemClock

import javax.inject.Inject

class SystemTimeProvider @Inject constructor() : TimeProvider {

    override fun getElapsedRealtime(): Long = SystemClock.elapsedRealtime()
}