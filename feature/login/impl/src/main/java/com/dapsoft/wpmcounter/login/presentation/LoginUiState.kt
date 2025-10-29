package com.dapsoft.wpmcounter.login.presentation

/**
 * Immutable UI state for the Login screen. Single source of truth for the user name input field.
 *
 * Invariants:
 *  - [userName] reflects the last user-entered (possibly normalized) value.
 */
internal data class LoginUiState(val userName: String)
