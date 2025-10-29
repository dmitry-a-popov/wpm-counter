package com.dapsoft.wpmcounter.typing.presentation

internal sealed class TypingUiIntent {

    data class ChangeTypedText(val text: String) : TypingUiIntent()

    object ChangeUser: TypingUiIntent()

    object Restart: TypingUiIntent()
}
