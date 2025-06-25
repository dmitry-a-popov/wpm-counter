package com.dapsoft.wpmcounter.common

import android.content.Context

import com.dapsoft.wpmcounter.common.orientation.ScreenOrientation
import com.dapsoft.wpmcounter.common.orientation.ScreenOrientationProvider

internal class ScreenOrientationProviderImpl(private val context: Context) : ScreenOrientationProvider {

    override fun getCurrentOrientation(): ScreenOrientation {
        return ScreenOrientation.fromConfigValue(context.resources.configuration.orientation)
    }
}