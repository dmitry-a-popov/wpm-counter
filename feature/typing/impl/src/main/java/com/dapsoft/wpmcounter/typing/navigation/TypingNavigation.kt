package com.dapsoft.wpmcounter.typing.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

import com.dapsoft.wpmcounter.typing.ui.TypingScreen

fun NavGraphBuilder.typingScreen(
    onLogout: () -> Unit
) {
    composable(TypingRoute.ROUTE) { backStackEntry ->
        TypingScreen(viewModel = hiltViewModel(backStackEntry), onChangeUser = onLogout)
    }
}
