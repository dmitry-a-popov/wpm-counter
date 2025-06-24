package com.dapsoft.wpmcounter.common.screenorientation

interface ScreenOrientationProvider {

    fun getCurrentOrientation(): ScreenOrientation
}