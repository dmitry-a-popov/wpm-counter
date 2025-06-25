package com.dapsoft.wpmcounter.analytics

interface ClearEventsUseCase {

    /**
     * Clears all analytics events from the repository.
     * This operation cannot be undone.
     */
    suspend operator fun invoke()
}