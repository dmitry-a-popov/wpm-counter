package com.dapsoft.wpmcounter.common.orientation

import android.content.Context
import android.content.res.Configuration

import dagger.hilt.android.qualifiers.ApplicationContext

import javax.inject.Inject

/**
 * Android-backed implementation of [ScreenOrientationProvider].
 */
internal class ScreenOrientationProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ScreenOrientationProvider {

    override val currentOrientation: ScreenOrientation
        get() = context.resources.configuration.toScreenOrientation()
}

internal fun Configuration.toScreenOrientation(): ScreenOrientation = when (orientation) {
    Configuration.ORIENTATION_PORTRAIT -> ScreenOrientation.PORTRAIT
    Configuration.ORIENTATION_LANDSCAPE -> ScreenOrientation.LANDSCAPE
    else -> ScreenOrientation.UNDEFINED
}
