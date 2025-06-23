package com.dapsoft.wpmcounter.analytics

interface TrackKeyPressUseCase {

    operator fun invoke(keyCode: Int, eventTimeMillis: Long, phoneOrientation: Int, username: String)
}