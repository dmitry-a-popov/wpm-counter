package com.dapsoft.wpmcounter.analytics.impl.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import kotlinx.coroutines.flow.Flow

@Dao
interface KeyEventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: KeyEventEntity)

    @Query("SELECT * FROM key_events ORDER BY timestamp DESC")
    fun getAllEvents(): Flow<List<KeyEventEntity>>

    @Query("SELECT * FROM key_events WHERE username = :username ORDER BY timestamp DESC")
    fun getEventsByUser(username: String): Flow<List<KeyEventEntity>>

    @Query("SELECT * FROM key_events ORDER BY timestamp DESC LIMIT 1")
    fun getLatestEvent(): Flow<KeyEventEntity?>

    @Query("DELETE FROM key_events")
    suspend fun deleteAllEvents()
}