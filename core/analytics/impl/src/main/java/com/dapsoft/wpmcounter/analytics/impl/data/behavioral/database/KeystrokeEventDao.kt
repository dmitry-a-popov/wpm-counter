package com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for keyboard event analytics data.
 * Provides methods to store and retrieve keyboard interaction events.
 */
@Dao
internal interface KeystrokeEventDao {
    /**
     * Stores a keyboard event in the database.
     * Replaces any existing event with the same ID.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: KeystrokeEventEntity)

    /**
     * Retrieves the most recent keyboard event, if any exists.
     */
    @Query("SELECT * FROM key_events ORDER BY eventTimeMillis DESC LIMIT 1")
    fun observeLatestEvent(): Flow<KeystrokeEventEntity?>

    /**
     * Deletes all keyboard events from the database.
     * This operation cannot be undone.
     */
    @Query("DELETE FROM key_events")
    suspend fun deleteAllEvents()
}
