package com.dapsoft.wpmcounter.login.presentation

/**
 * User (and potential system) intents for the Login screen in a lightweight MVI model.
 * Each intent expresses an action request; it contains no business logic.
 * Handled sequentially inside [LoginViewModel.reduce].
 *
 * Guidelines:
 *  - Intents are processed one at a time -> simpler reasoning about state transitions.
 *  - They never return a value directly; results are reflected via [LoginUiState] updates or [LoginOneTimeEvent] emissions.
 */
internal sealed class LoginUiIntent {

    /**
     * User changed the text input for the user name.
     */
    data class ChangeUserName(val name: String) : LoginUiIntent()

    /**
     * User confirmed the form: triggers persistence and on success navigation.
     */
    object ConfirmLogin : LoginUiIntent()
}
