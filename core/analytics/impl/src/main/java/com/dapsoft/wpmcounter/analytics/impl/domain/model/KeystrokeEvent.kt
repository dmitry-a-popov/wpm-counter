package com.dapsoft.wpmcounter.analytics.impl.domain.model

import com.dapsoft.wpmcounter.common.orientation.ScreenOrientation

import kotlin.time.Duration

data class KeystrokeEvent(
    val eventTime: Duration,
    val symbol: Char,
    val phoneOrientation: ScreenOrientation,
    val username: String
)