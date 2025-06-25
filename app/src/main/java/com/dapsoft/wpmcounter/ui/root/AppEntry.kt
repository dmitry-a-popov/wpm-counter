package com.dapsoft.wpmcounter.ui.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.rememberNavController

import com.dapsoft.wpmcounter.login.navigation.LoginRoute
import com.dapsoft.wpmcounter.typing.navigation.TypingRoute
import com.dapsoft.wpmcounter.ui.AppNavHost
import com.dapsoft.wpmcounter.ui.root.state.StartupState
import com.dapsoft.wpmcounter.user.GetUserNameUseCase

import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

import javax.inject.Inject

@HiltViewModel
class AppEntryViewModel @Inject constructor(
    private val getUserNameUseCase: GetUserNameUseCase
) : ViewModel() {

    val startupState = getUserNameUseCase()
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

@Composable
fun AppEntry(
    vm: AppEntryViewModel = hiltViewModel()
) {
    val startupState by vm.startupState.collectAsState(initial = StartupState.Loading)
    val navController = rememberNavController()

    when (startupState) {
        StartupState.Loading -> SplashScreen()
        StartupState.UserRequired -> AppNavHost(navController, LoginRoute.ROUTE)
        StartupState.UserAvailable -> AppNavHost(navController, TypingRoute.ROUTE)
    }
}
