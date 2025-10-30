package com.dapsoft.wpmcounter.login.presentation

/**
 * One-off (ephemeral) effects emitted by the Login screen that must NOT be part of the persistent UI state.
 *
 * Typical use cases:
 *  - Navigation (leaving the screen after a successful name save)
 *  - Transient UI feedback (toast / snackbar on failure)
 *
 * Produced inside [LoginViewModel] via a dedicated effect flow and consumed exactly once by the UI layer
 * (e.g. collect in a `LaunchedEffect`). They are deliberately separated from [LoginUiState] to avoid:
 *  - Re-dispatching the same navigation / error after recomposition or configuration changes
 *  - Polluting immutable state with side-effect semantics
 */
internal sealed class LoginEffect {
    /**
     * Indicates the user name was saved successfully and the screen can be left.
     * UI should trigger navigation, using the latest stable state value.
     */
    object LeaveScreen : LoginEffect()

    /**
     * Indicates a validation or persistence error while saving the user name.
     * UI decides how to surface the message (Toast / Snackbar / banner etc.).
     */
    object ShowLoginError : LoginEffect()
}
