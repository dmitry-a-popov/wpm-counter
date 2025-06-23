package com.dapsoft.wpmcounter.common

import android.os.SystemClock

class SystemTimeProvider : TimeProvider {

    override fun getElapsedRealtime(): Long = SystemClock.elapsedRealtime()
}