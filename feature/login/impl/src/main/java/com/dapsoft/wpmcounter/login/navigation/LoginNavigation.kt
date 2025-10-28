package com.dapsoft.wpmcounter.login.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

import com.dapsoft.wpmcounter.login.ui.LoginScreen

/**
 * Registers the Login destination in the navigation graph.
 *
 * @param onLoginConfirmed callback invoked after successful login; receives the confirmed user name.
 */
fun NavGraphBuilder.registerLoginDestination(
    onLoginConfirmed: (String) -> Unit
) {
    composable(LoginRoute.ROUTE) { backStackEntry ->
        LoginScreen(hiltViewModel(backStackEntry), onLoginConfirmed)
    }
}
