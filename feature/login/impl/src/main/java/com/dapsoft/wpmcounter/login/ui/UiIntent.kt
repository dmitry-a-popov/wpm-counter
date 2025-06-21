package com.dapsoft.wpmcounter.login.ui

internal sealed class UiIntent {

    data class ChangeUserName(val name: String) : UiIntent()

    object ConfirmLogin : UiIntent()
}