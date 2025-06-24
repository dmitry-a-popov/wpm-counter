package com.dapsoft.wpmcounter.analytics

import com.dapsoft.wpmcounter.common.screenorientation.ScreenOrientation

interface TrackKeyPressUseCase {

    suspend operator fun invoke(
        keyCode: Char,
        eventTimeMillis: Long,
        phoneOrientation: ScreenOrientation,
        username: String
    )
}