package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.analytics.impl.domain.model.KeystrokeEvent

import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing behavioral analytics data, specifically keyboard interaction events.
 * Provides methods for storing, retrieving and managing keystroke event records.
 */
internal interface BehavioralAnalyticsRepository {
    /**
     * Saves a keyboard event to the repository.
     *
     * @param event The keystroke event to save
     */
    suspend fun saveEvent(event: KeystrokeEvent)

    /**
     * Retrieves the most recent keystroke event, if available.
     *
     * @return Flow emitting the latest keystroke event or null if none exists
     */
    fun observeLatestEvent(): Flow<KeystrokeEvent?>

    /**
     * Deletes all stored keystroke events.
     */
    suspend fun deleteAllEvents()
}
