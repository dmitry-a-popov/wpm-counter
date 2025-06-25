package com.dapsoft.wpmcounter.analytics.impl.data.behavioral

import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database.KeyEventEntity

import kotlinx.coroutines.flow.Flow

/**
 * Data source providing access to behavioral analytics storage.
 * Handles database operations for key events tracking.
 */
internal interface BehavioralAnalyticsDataSource {
    /**
     * Stores a key event in the analytics data source.
     *
     * @param event The key event data entity to store
     */
    suspend fun saveKeyEventEntity(event: KeyEventEntity)

    /**
     * Retrieves the most recent key event, if available.
     *
     * @return Flow emitting the latest key event or null if none exists
     */
    fun getLatestKeyEventEntity(): Flow<KeyEventEntity?>

    /**
     * Removes all stored key events.
     */
    suspend fun deleteAllKeyEventEntities()
}