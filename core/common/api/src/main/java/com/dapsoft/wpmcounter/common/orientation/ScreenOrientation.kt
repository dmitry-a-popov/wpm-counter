package com.dapsoft.wpmcounter.common.orientation

import android.content.res.Configuration

/**
 * Domain representation of screen orientation.
 */
enum class ScreenOrientation(val value: Int) {
    PORTRAIT(Configuration.ORIENTATION_PORTRAIT),
    LANDSCAPE(Configuration.ORIENTATION_LANDSCAPE),
    /**
     * Orientation could not be determined (e.g., transient state).
     */
    UNDEFINED(-1);

    companion object {
        fun fromConfigValue(configOrientation: Int): ScreenOrientation = when(configOrientation) {
            Configuration.ORIENTATION_PORTRAIT -> PORTRAIT
            Configuration.ORIENTATION_LANDSCAPE -> LANDSCAPE
            else -> UNDEFINED
        }
    }
}
