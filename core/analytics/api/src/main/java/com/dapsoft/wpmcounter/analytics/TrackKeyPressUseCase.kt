package com.dapsoft.wpmcounter.analytics

interface TrackKeyPressUseCase {

    operator fun invoke(keyCode: Char, phoneOrientation: Int, username: String)
}