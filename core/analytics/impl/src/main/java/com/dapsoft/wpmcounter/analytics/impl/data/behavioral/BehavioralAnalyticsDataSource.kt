package com.dapsoft.wpmcounter.analytics.impl.data.behavioral

import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database.KeystrokeEventEntity

import kotlinx.coroutines.flow.Flow

/**
 * Data source providing access to behavioral analytics storage.
 * Handles database operations for keystroke events tracking.
 */
internal interface BehavioralAnalyticsDataSource {
    /**
     * Stores a keystroke event in the analytics data source.
     *
     * @param event The keystroke event data entity to store
     */
    suspend fun saveKeystrokeEventEntity(event: KeystrokeEventEntity)

    /**
     * Retrieves the most recent keystroke event, if available.
     *
     * @return Flow emitting the latest keystroke event or null if none exists
     */
    fun getLatestKeystrokeEventEntity(): Flow<KeystrokeEventEntity?>

    /**
     * Removes all stored keystroke events.
     */
    suspend fun deleteAllKeystrokeEventEntities()
}