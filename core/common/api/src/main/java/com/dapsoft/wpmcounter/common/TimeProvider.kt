package com.dapsoft.wpmcounter.common

interface TimeProvider {

    fun getElapsedRealtime(): Long
}