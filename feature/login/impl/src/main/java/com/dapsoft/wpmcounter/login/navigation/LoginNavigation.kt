package com.dapsoft.wpmcounter.login.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

import com.dapsoft.wpmcounter.login.ui.LoginScreen

fun NavGraphBuilder.loginScreen(
    onLoginConfirmed: (String) -> Unit
) {
    composable(LoginRoute.ROUTE) { backStackEntry ->
        LoginScreen(hiltViewModel(backStackEntry), onLoginConfirmed)
    }
}
