package com.dapsoft.wpmcounter.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController

import com.dapsoft.wpmcounter.login.navigation.LoginRoute
import com.dapsoft.wpmcounter.typing.navigation.TypingRoute
import com.dapsoft.wpmcounter.login.navigation.navigateToLogin
import com.dapsoft.wpmcounter.typing.navigation.navigateToTyping

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(navController = navController, startDestination = startDestination) {
        navigateToLogin { user ->
            navController.navigate(TypingRoute.ROUTE)
        }
        navigateToTyping {
            navController.navigate(LoginRoute.ROUTE)
        }
    }
}