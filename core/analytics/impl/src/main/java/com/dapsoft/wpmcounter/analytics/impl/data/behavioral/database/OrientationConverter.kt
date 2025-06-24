package com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database

import androidx.room.TypeConverter

import com.dapsoft.wpmcounter.common.screenorientation.ScreenOrientation

class OrientationConverter {
    @TypeConverter
    fun toInt(orientation: ScreenOrientation): Int = orientation.value

    @TypeConverter
    fun toOrientation(value: Int): ScreenOrientation {
        return ScreenOrientation.fromConfigValue(value)
    }
}