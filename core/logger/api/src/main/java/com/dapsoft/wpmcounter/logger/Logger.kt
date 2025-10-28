package com.dapsoft.wpmcounter.logger

enum class LogLevel { VERBOSE, DEBUG, INFO, WARN, ERROR, WTF }

interface Logger {

    /**
     * Core logging entry point.
     *
     * @param level log severity
     * @param tag short source tag
     * @param throwable optional error
     * @param message lazy message supplier
     */
    fun log(
        level: LogLevel,
        tag: String,
        throwable: Throwable? = null,
        message: () -> String
    )
}

inline fun Logger.v(
    tag: String,
    throwable: Throwable? = null,
    crossinline message: () -> String
) {
    log(LogLevel.VERBOSE, tag, throwable) { message() }
}

inline fun Logger.d(
    tag: String,
    throwable: Throwable? = null,
    crossinline message: () -> String
) {
    log(LogLevel.DEBUG, tag, throwable) { message() }
}

inline fun Logger.i(
    tag: String,
    throwable: Throwable? = null,
    crossinline message: () -> String
) {
    log(LogLevel.INFO, tag, throwable) { message() }
}

inline fun Logger.w(
    tag: String,
    throwable: Throwable? = null,
    crossinline message: () -> String
) {
    log(LogLevel.WARN, tag, throwable) { message() }
}

inline fun Logger.e(
    tag: String,
    throwable: Throwable? = null,
    crossinline message: () -> String
) {
    log(LogLevel.ERROR, tag, throwable) { message() }
}

inline fun Logger.wtf(
    tag: String,
    throwable: Throwable? = null,
    crossinline message: () -> String
) {
    log(LogLevel.WTF, tag, throwable) { message() }
}
