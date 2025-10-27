package com.dapsoft.wpmcounter.common

import android.content.Context

import com.dapsoft.wpmcounter.common.orientation.ScreenOrientation
import com.dapsoft.wpmcounter.common.orientation.ScreenOrientationProvider

import dagger.hilt.android.qualifiers.ApplicationContext

import javax.inject.Inject

/**
 * Android-backed implementation of [ScreenOrientationProvider].
 */
internal class ScreenOrientationProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ScreenOrientationProvider {

    override val currentOrientation: ScreenOrientation
        get() = ScreenOrientation.fromConfigValue(context.resources.configuration.orientation)
}
