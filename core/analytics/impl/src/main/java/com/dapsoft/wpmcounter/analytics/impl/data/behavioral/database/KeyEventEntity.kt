package com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database

import androidx.room.Entity
import androidx.room.PrimaryKey

import com.dapsoft.wpmcounter.common.orientation.ScreenOrientation

/**
 * Database entity representing a keyboard interaction event.
 * Stores timing and contextual information about key presses.
 *
 * @property id Auto-generated unique identifier for the event
 * @property keyPressTime Timestamp when the key was pressed (in milliseconds since device startup)
 * @property keyReleaseTime Timestamp when the key was released (in milliseconds since device startup)
 * @property keyCode Character representation of the pressed key
 * @property phoneOrientation Device orientation when the key was pressed
 * @property username Identifier of the user who performed the action
 */
@Entity(tableName = "key_events")
internal data class KeyEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val keyPressTime: Long,
    val keyReleaseTime: Long,
    val keyCode: Char,
    val phoneOrientation: ScreenOrientation,
    val username: String
)
