package com.dapsoft.wpmcounter.login.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

import com.dapsoft.wpmcounter.login.ui.LoginScreen

fun NavGraphBuilder.navigateToLogin(
    onLoginConfirmed: (String) -> Unit
) {
    composable(LoginRoute.ROUTE) {
        LoginScreen(onLoginConfirmed, hiltViewModel())
    }
}