package com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database

import androidx.room.TypeConverter

import com.dapsoft.wpmcounter.common.orientation.ScreenOrientation

/**
 * Room TypeConverter for persisting ScreenOrientation enum values.
 * Handles conversion between database String representation and domain model.
 */
internal class OrientationConverter {
    /**
     * Converts a ScreenOrientation enum to its String representation for storage.
     *
     * @param orientation The orientation to convert
     * @return String value representing the orientation
     */
    @TypeConverter
    fun fromOrientation(orientation: ScreenOrientation): String = orientation.code

    /**
     * Converts a stored String value back to a ScreenOrientation enum.
     *
     * @param value The stored String value
     * @return The corresponding ScreenOrientation
     */
    @TypeConverter
    fun toOrientation(value: String): ScreenOrientation {
        return ScreenOrientation.fromCode(value)
    }
}
