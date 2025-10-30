package com.dapsoft.wpmcounter.typing.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

import com.dapsoft.wpmcounter.typing.ui.TypingScreen

/**
 * Registers the Typing destination in the navigation graph.
 *
 * @param onLogout callback invoked after user logout.
 */
fun NavGraphBuilder.registerTypingDestination(
    onLogout: () -> Unit
) {
    composable(TypingRoute.ROUTE) { backStackEntry ->
        TypingScreen(viewModel = hiltViewModel(backStackEntry), onChangeUser = onLogout)
    }
}
