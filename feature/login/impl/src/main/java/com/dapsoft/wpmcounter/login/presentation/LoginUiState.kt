package com.dapsoft.wpmcounter.login.presentation

import androidx.compose.runtime.Immutable

/**
 * Immutable UI state for the Login screen. Single source of truth for the user name input field.
 *
 * Invariants:
 *  - [userName] reflects the last user-entered (possibly normalized) value.
 */
@Immutable
internal data class LoginUiState(val userName: String)
