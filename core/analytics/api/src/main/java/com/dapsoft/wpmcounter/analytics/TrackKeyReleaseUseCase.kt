package com.dapsoft.wpmcounter.analytics

interface TrackKeyReleaseUseCase {

    //TODO Use keyCode as Integer instead of Char
    suspend operator fun invoke(keyCode: Char, phoneOrientation: Int, username: String)
}