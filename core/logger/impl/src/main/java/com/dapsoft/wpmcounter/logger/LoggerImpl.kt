package com.dapsoft.wpmcounter.logger

import android.util.Log

/**
 * Android implementation.
 * Supports simple minLevel filtering.
 */
internal class LoggerImpl(private val minLevel: LogLevel) : Logger {

    override fun log(
        level: LogLevel,
        tag: String,
        throwable: Throwable?,
        message: () -> String
    ) {
        if (level.ordinal < minLevel.ordinal) return
        
        val msg = runCatching { message() }.getOrElse { "Log message supplier threw: ${it.message}" }

        when (level) {
            LogLevel.VERBOSE -> if (throwable == null) Log.v(tag, msg) else Log.v(tag, msg, throwable)
            LogLevel.DEBUG   -> if (throwable == null) Log.d(tag, msg) else Log.d(tag, msg, throwable)
            LogLevel.INFO    -> if (throwable == null) Log.i(tag, msg) else Log.i(tag, msg, throwable)
            LogLevel.WARN    -> if (throwable == null) Log.w(tag, msg) else Log.w(tag, msg, throwable)
            LogLevel.ERROR   -> if (throwable == null) Log.e(tag, msg) else Log.e(tag, msg, throwable)
            LogLevel.WTF     -> if (throwable == null) Log.wtf(tag, msg) else Log.wtf(tag, msg, throwable)
        }
    }
}
