package com.dapsoft.wpmcounter.analytics.impl.domain.model

import com.dapsoft.wpmcounter.common.orientation.ScreenOrientation

import kotlin.time.Duration

/**
 * Represents a single user keystroke for behavioral analytics.
 *
 * @property eventTime Monotonic elapsed real time (since boot) when the key was pressed.
 * @property symbol The character pressed.
 * @property screenOrientation Orientation at the moment of the event.
 * @property userName Display/user name at the moment of the event.
 */
internal data class KeystrokeEvent(
    val eventTime: Duration,
    val symbol: Char,
    val screenOrientation: ScreenOrientation,
    val userName: String
)
