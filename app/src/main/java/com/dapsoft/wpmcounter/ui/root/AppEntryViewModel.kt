package com.dapsoft.wpmcounter.ui.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.dapsoft.wpmcounter.user.UserRepository

import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

import javax.inject.Inject

@HiltViewModel
class AppEntryViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val startupState = userRepository.name
        .map { name ->
            when {
                name.isBlank() -> StartupState.UserRequired
                else -> StartupState.UserAvailable
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            StartupState.Loading
        )
}
