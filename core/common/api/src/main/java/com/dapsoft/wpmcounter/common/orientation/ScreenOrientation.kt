package com.dapsoft.wpmcounter.common.orientation

/**
 * Domain representation of screen orientation.
 */
enum class ScreenOrientation(val code: String) {
    PORTRAIT("portrait"),
    LANDSCAPE("landscape"),
    UNDEFINED("undefined");

    companion object {
        fun fromCode(code: String?): ScreenOrientation =
            entries.firstOrNull { it.code == code } ?: UNDEFINED
    }
}
