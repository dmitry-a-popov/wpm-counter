package com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Room database for storing analytics events.
 *
 * This database maintains behavioral analytics data like key press events.
 * Schema migrations should be added when modifying the database structure.
 */
@Database(
    entities = [KeyEventEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(OrientationConverter::class)
internal abstract class AnalyticsDatabase : RoomDatabase() {
    /**
     * Provides access to key event operations in the database.
     */
    abstract fun keyEventDao(): KeyEventDao
}