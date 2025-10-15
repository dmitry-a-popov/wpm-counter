package com.dapsoft.wpmcounter.analytics

import com.dapsoft.wpmcounter.common.orientation.ScreenOrientation

interface TrackKeyPressUseCase {

    /**
     * Records a key press event with associated metadata.
     *
     * @param symbol The character representation of the pressed key
     * @param eventTimeMillis Timestamp when the key press occurred (in milliseconds since device startup)
     * @param phoneOrientation Current orientation of the device when event occurred
     * @param username Identifier of the user performing the action
     */
    suspend operator fun invoke(
        symbol: Char,
        eventTimeMillis: Long,
        phoneOrientation: ScreenOrientation,
        username: String
    )
}