package com.dapsoft.wpmcounter.common.orientation

import android.content.res.Configuration

enum class ScreenOrientation(val value: Int) {
    PORTRAIT(Configuration.ORIENTATION_PORTRAIT),
    LANDSCAPE(Configuration.ORIENTATION_LANDSCAPE),
    UNDEFINED(-1);

    companion object {
        fun fromConfigValue(configOrientation: Int): ScreenOrientation = when(configOrientation) {
            Configuration.ORIENTATION_PORTRAIT -> PORTRAIT
            Configuration.ORIENTATION_LANDSCAPE -> LANDSCAPE
            else -> UNDEFINED
        }
    }
}