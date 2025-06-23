package com.dapsoft.wpmcounter.typing.ui

sealed class UiIntent {

    data class ChangeTypedText(val text: String) : UiIntent()

    data class KeyPress(val keyCode: Int, val eventTime: Long) : UiIntent()

    data class KeyRelease(val keyCode: Int, val eventTime: Long) : UiIntent()

    object ChangeUser: UiIntent()

    object Restart: UiIntent()
}