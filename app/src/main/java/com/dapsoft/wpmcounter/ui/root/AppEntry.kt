package com.dapsoft.wpmcounter.ui.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController

import com.dapsoft.wpmcounter.login.navigation.LoginRoute
import com.dapsoft.wpmcounter.typing.navigation.TypingRoute
import com.dapsoft.wpmcounter.ui.AppNavHost

@Composable
fun AppEntry(
    viewModel: AppEntryViewModel = hiltViewModel()
) {
    val startupState by viewModel.startupState.collectAsState(initial = StartupState.Loading)
    val navController = rememberNavController()

    when (startupState) {
        StartupState.Loading -> SplashScreen()
        StartupState.UserRequired -> AppNavHost(navController, LoginRoute.ROUTE)
        StartupState.UserAvailable -> AppNavHost(navController, TypingRoute.ROUTE)
    }
}
