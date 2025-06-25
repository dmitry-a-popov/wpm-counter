package com.dapsoft.wpmcounter.analytics.impl.domain.model

import com.dapsoft.wpmcounter.common.orientation.ScreenOrientation

data class KeyEvent(
    val keyPressTime: Long,
    val keyReleaseTime: Long,
    val keyCode: Char,
    val phoneOrientation: ScreenOrientation,
    val username: String
)