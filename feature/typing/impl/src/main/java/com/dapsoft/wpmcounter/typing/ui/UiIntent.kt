package com.dapsoft.wpmcounter.typing.ui

sealed class UiIntent {

    data class ChangeTypedText(val text: String) : UiIntent()

    object ChangeUser: UiIntent()
}