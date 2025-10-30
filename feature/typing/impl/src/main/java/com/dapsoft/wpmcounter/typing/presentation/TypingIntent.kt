package com.dapsoft.wpmcounter.typing.presentation

/**
 * Intents (user-driven or system-triggered actions) consumed by TypingViewModel.
 * They are one-shot commands; reducer derives new UI state from them.
 * @see TypingViewModel.reduce
 */
internal sealed interface TypingIntent {

    /**
     * User changed the whole typed text (full snapshot).
     * The text is expected to be raw input; no trimming is applied here.
     */
    data class ChangeTypedText(val text: String) : TypingIntent

    /** User requested changing the current user/profile. */
    object ChangeUser: TypingIntent

    /** User requested restarting the typing session. */
    object Restart: TypingIntent
}
