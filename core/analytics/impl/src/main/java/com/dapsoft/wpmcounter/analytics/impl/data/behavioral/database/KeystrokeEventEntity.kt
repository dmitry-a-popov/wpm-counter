package com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database

import androidx.room.Entity
import androidx.room.PrimaryKey

import com.dapsoft.wpmcounter.common.orientation.ScreenOrientation

/**
 * Database entity representing a keyboard interaction event.
 * Stores timing and contextual information about key presses.
 *
 * @property id Auto-generated unique identifier for the event
 * @property eventTimeMillis Timestamp when the key was pressed (in milliseconds since device startup)
 * @property symbol Character representation of the pressed key
 * @property screenOrientation Device orientation when the key was pressed
 * @property userName Identifier of the user who performed the action
 */
@Entity(tableName = "key_events")
internal data class KeystrokeEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val eventTimeMillis: Long,
    val symbol: Char,
    val screenOrientation: ScreenOrientation,
    val userName: String
)
