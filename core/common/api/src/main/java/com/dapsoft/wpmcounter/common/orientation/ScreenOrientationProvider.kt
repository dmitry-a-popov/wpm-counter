package com.dapsoft.wpmcounter.common.orientation

/**
 * Provides a snapshot of the current screen orientation at call time.
 */
interface ScreenOrientationProvider {

    /**
     * Returns the current orientation. May be [ScreenOrientation.UNDEFINED] if the system
     * cannot determine orientation yet.
     */
    val currentOrientation: ScreenOrientation
}
