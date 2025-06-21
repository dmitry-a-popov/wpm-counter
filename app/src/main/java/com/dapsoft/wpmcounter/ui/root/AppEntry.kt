package com.dapsoft.wpmcounter.ui.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.rememberNavController
import com.dapsoft.wpmcounter.login.navigation.LoginRoute
import com.dapsoft.wpmcounter.typing.navigation.TypingRoute
import com.dapsoft.wpmcounter.ui.AppNavHost

import com.dapsoft.wpmcounter.ui.root.state.StartupState
import com.dapsoft.wpmcounter.user.GetUserNameUseCase

import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.map

import javax.inject.Inject

@HiltViewModel
class AppEntryViewModel @Inject constructor(
    val getUserNameUseCase: GetUserNameUseCase
) : ViewModel()

@Composable
fun AppEntry(
    vm: AppEntryViewModel = hiltViewModel()
) {
    val startupState by vm.getUserNameUseCase.invoke()
        .map { name ->
            when {
                name.isBlank() -> StartupState.NeedsUser
                else -> StartupState.HasUser
            }
        }
        .collectAsState(initial = StartupState.Loading)

    val navController = rememberNavController()

    when (startupState) {
        StartupState.Loading -> SplashScreen()
        StartupState.NeedsUser -> AppNavHost(navController, LoginRoute.ROUTE)
        StartupState.HasUser -> AppNavHost(navController, TypingRoute.ROUTE)
    }
}
