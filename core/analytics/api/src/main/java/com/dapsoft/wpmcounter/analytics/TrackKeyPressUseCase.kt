package com.dapsoft.wpmcounter.analytics

import com.dapsoft.wpmcounter.common.orientation.ScreenOrientation

import kotlin.time.Duration

interface TrackKeyPressUseCase {

    /**
     * Records a key press event with associated metadata.
     *
     * @param symbol The character representation of the pressed key
     * @param eventTime Timestamp when the key press occurred (since device startup)
     * @param screenOrientation Current orientation of the device when event occurred
     * @param username Identifier of the user performing the action
     */
    suspend operator fun invoke(
        symbol: Char,
        eventTime: Duration,
        screenOrientation: ScreenOrientation,
        username: String
    )
}