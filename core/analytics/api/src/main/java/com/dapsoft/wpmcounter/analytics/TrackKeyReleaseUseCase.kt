package com.dapsoft.wpmcounter.analytics

interface TrackKeyReleaseUseCase {

    suspend operator fun invoke(keyCode: Int, eventTimeMillis: Long, phoneOrientation: Int, username: String)
}