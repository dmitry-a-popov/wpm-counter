package com.dapsoft.wpmcounter.analytics

interface ClearEventsUseCase {

    /**
     * Clears all analytics events from the repository.
     * This operation cannot be undone.
     *
     * @return `Result.success(Unit)` if clearing completed (even if there was nothing to remove),
     * or `Result.failure(cause)` on error (including cancellation).
     */
    suspend operator fun invoke(): Result<Unit>
}
