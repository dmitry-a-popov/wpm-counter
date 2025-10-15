package com.dapsoft.wpmcounter.analytics.impl.domain.model

import com.dapsoft.wpmcounter.common.orientation.ScreenOrientation

data class KeyEvent(
    val eventTimeMillis: Long,
    val symbol: Char,
    val phoneOrientation: ScreenOrientation,
    val username: String
)