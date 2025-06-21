package com.dapsoft.wpmcounter.ui.root.state

sealed interface StartupState {

    object Loading : StartupState

    object NeedsUser : StartupState

    object HasUser : StartupState
}