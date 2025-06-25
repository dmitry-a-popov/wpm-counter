package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.analytics.impl.domain.model.KeyEvent

import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing behavioral analytics data, specifically keyboard interaction events.
 * Provides methods for storing, retrieving and managing key event records.
 */
internal interface BehavioralAnalyticsRepository {
    /**
     * Saves a keyboard event to the repository.
     *
     * @param event The key event to save
     */
    suspend fun saveKeyEvent(event: KeyEvent)

    /**
     * Retrieves the most recent key event, if available.
     *
     * @return Flow emitting the latest key event or null if none exists
     */
    fun getLatestEvent(): Flow<KeyEvent?>

    /**
     * Deletes all stored key events.
     */
    suspend fun deleteAllEvents()
}