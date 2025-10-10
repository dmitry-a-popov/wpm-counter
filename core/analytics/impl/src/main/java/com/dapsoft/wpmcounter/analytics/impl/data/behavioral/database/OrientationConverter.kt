package com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database

import androidx.room.TypeConverter

import com.dapsoft.wpmcounter.common.orientation.ScreenOrientation

/**
 * Room TypeConverter for persisting ScreenOrientation enum values.
 * Handles conversion between database integer representation and domain model.
 */
internal class OrientationConverter {
    /**
     * Converts a ScreenOrientation enum to its integer representation for storage.
     *
     * @param orientation The orientation to convert
     * @return Integer value representing the orientation
     */
    @TypeConverter
    fun toInt(orientation: ScreenOrientation): Int = orientation.value

    /**
     * Converts a stored integer value back to a ScreenOrientation enum.
     *
     * @param value The stored integer value
     * @return The corresponding ScreenOrientation
     */
    @TypeConverter
    fun toOrientation(value: Int): ScreenOrientation {
        return ScreenOrientation.fromConfigValue(value)
    }
}