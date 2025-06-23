package com.dapsoft.wpmcounter.analytics.impl.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [KeyEventEntity::class], version = 1)
internal abstract class AnalyticsDatabase : RoomDatabase() {
    abstract fun keyEventDao(): KeyEventDao
}