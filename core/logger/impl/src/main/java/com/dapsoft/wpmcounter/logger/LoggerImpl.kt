package com.dapsoft.wpmcounter.logger

import android.util.Log

internal class LoggerImpl : Logger {

    override fun v(tag: String, msg: String) {
        Log.v(tag, msg)
    }

    override fun d(tag: String, msg: String) {
        Log.d(tag, msg)
    }

    override fun i(tag: String, msg: String) {
        Log.i(tag, msg)
    }

    override fun w(tag: String, msg: String) {
        Log.w(tag, msg)
    }

    override fun e(tag: String, msg: String) {
        Log.e(tag, msg)
    }

    override fun e(tag: String, msg: String, err: Throwable) {
        Log.e(tag, msg, err)
    }

    override fun wtf(tag: String, msg: String) {
        Log.wtf(tag, msg)
    }

    override fun wtf(tag: String, msg: String, err: Throwable) {
        Log.wtf(tag, msg, err)
    }
}