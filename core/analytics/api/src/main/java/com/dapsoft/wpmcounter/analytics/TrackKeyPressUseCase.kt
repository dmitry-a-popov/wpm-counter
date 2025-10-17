package com.dapsoft.wpmcounter.analytics

interface TrackKeyPressUseCase {

    /**
     * Records a key press event with associated metadata.
     *
     * @param symbol The character representation of the pressed key
     * @param username Identifier of the user performing the action
     */
    suspend operator fun invoke(
        symbol: Char,
        username: String
    )
}
