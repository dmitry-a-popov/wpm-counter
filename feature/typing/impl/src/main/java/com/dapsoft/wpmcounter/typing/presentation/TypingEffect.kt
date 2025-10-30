package com.dapsoft.wpmcounter.typing.presentation

/**
 * One-shot side effects emitted by TypingViewModel that should not be persisted in UI state.
 * Collected once and acted upon (e.g., navigation).
 */
internal sealed class TypingEffect {
    /** Navigate away from the typing screen (e.g., user cleared). */
    object LeaveScreen : TypingEffect()
}
