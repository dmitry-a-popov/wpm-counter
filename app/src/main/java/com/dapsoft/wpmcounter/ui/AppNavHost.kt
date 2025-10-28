package com.dapsoft.wpmcounter.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController

import com.dapsoft.wpmcounter.login.navigation.LoginRoute
import com.dapsoft.wpmcounter.typing.navigation.TypingRoute
import com.dapsoft.wpmcounter.login.navigation.registerLoginDestination
import com.dapsoft.wpmcounter.typing.navigation.registerTypingDestination

/**
 * Main navigation host for the application
 * @param navController The navigation controller to use
 * @param startDestination The initial destination route
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(navController = navController, startDestination = startDestination) {
        registerLoginDestination(
            onLoginConfirmed = {
                navController.navigate(TypingRoute.ROUTE) {
                    popUpTo(LoginRoute.ROUTE) { inclusive = true }
                }
            }
        )
        registerTypingDestination(
            onLogout = {
                navController.navigate(LoginRoute.ROUTE) {
                    popUpTo(0) { inclusive = true }
                }
            }
        )
    }
}
