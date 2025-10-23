package com.dapsoft.wpmcounter.common

import android.content.Context

import com.dapsoft.wpmcounter.common.orientation.ScreenOrientation
import com.dapsoft.wpmcounter.common.orientation.ScreenOrientationProvider

/**
 * Android-backed implementation of [ScreenOrientationProvider].
 */
internal class ScreenOrientationProviderImpl(private val context: Context) : ScreenOrientationProvider {

    override val currentOrientation: ScreenOrientation
        get() = ScreenOrientation.fromConfigValue(context.resources.configuration.orientation)
}
