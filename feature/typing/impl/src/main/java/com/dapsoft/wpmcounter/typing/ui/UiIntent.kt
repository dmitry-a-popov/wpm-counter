package com.dapsoft.wpmcounter.typing.ui

sealed class UiIntent {

    data class ChangeTypedText(val text: String) : UiIntent()

    data class KeyPress(val key: Char) : UiIntent()

    data class KeyRelease(val key: Char) : UiIntent()

    object ChangeUser: UiIntent()

    object Restart: UiIntent()
}