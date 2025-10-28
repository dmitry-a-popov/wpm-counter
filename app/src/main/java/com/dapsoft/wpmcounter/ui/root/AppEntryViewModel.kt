package com.dapsoft.wpmcounter.ui.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.dapsoft.wpmcounter.login.navigation.LoginRoute
import com.dapsoft.wpmcounter.typing.navigation.TypingRoute
import com.dapsoft.wpmcounter.user.UserRepository

import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

import javax.inject.Inject

@HiltViewModel
class AppEntryViewModel @Inject constructor(
    userRepository: UserRepository
) : ViewModel() {

    val startDestination = userRepository.observeUserName()
        .map { name ->
            if (name.isBlank()) LoginRoute.ROUTE else TypingRoute.ROUTE
        }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
}
