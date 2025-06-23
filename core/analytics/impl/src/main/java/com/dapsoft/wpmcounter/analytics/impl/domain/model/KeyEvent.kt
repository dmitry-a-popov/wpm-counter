package com.dapsoft.wpmcounter.analytics.impl.domain.model

data class KeyEvent(
    val keyPressTime: Long,
    val keyReleaseTime: Long,
    val keyCode: Int,
    val phoneOrientation: Int,
    val username: String
)