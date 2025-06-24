package com.dapsoft.wpmcounter.common

import android.content.Context

import com.dapsoft.wpmcounter.common.screenorientation.ScreenOrientation
import com.dapsoft.wpmcounter.common.screenorientation.ScreenOrientationProvider

internal class ScreenOrientationProviderImpl(private val context: Context) : ScreenOrientationProvider {

    override fun getCurrentOrientation(): ScreenOrientation {
        return ScreenOrientation.fromConfigValue(context.resources.configuration.orientation)
    }
}