package com.dapsoft.wpmcounter.ui.root.state

/**
 * Represents the navigation state of the application during startup.
 */
sealed interface StartupState {
    /**
     * Initial state while determining if user exists.
     */
    object Loading : StartupState

    /**
     * The application requires user creation before proceeding.
     */
    object UserRequired : StartupState

    /**
     * User exists, proceed to main functionality.
     */
    object UserAvailable : StartupState
}