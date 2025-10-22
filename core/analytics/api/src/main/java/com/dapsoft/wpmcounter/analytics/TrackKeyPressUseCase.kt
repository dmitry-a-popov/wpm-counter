package com.dapsoft.wpmcounter.analytics

interface TrackKeyPressUseCase {

    /**
     * Records a key press event with associated metadata.
     *
     * @param symbol The character representation of the pressed key
     * @param userName Identifier of the user performing the action
     * @return `Result.success(Unit)` on successful recording, or `Result.failure(cause)` on error
     * (including `CancellationException` if cancelled).
     *
     */
    suspend operator fun invoke(
        symbol: Char,
        userName: String
    ) : Result<Unit>
}
