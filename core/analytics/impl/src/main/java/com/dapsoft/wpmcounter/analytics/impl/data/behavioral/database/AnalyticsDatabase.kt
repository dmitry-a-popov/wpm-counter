package com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [KeyEventEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(OrientationConverter::class)
internal abstract class AnalyticsDatabase : RoomDatabase() {
    abstract fun keyEventDao(): KeyEventDao
}