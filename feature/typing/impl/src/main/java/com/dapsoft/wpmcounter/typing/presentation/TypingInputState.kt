package com.dapsoft.wpmcounter.typing.presentation

/**
 * Input lifecycle states.
 */
enum class TypingInputState {
    ACTIVE,
    PAUSED,
    ERROR,
    COMPLETED;

    val isInputEnabled: Boolean
        get() = this == ACTIVE || this == PAUSED
}
