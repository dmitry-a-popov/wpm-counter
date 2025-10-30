package com.dapsoft.wpmcounter.typing.navigation

import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dapsoft.wpmcounter.typing.di.TypingUiEntryPoint
import com.dapsoft.wpmcounter.typing.presentation.TypingViewModel
import com.dapsoft.wpmcounter.typing.ui.TextMarker

import com.dapsoft.wpmcounter.typing.ui.TypingScreen
import dagger.hilt.android.EntryPointAccessors

/**
 * Registers the Typing destination in the navigation graph.
 *
 * @param onLogout callback invoked after user logout.
 */
fun NavGraphBuilder.registerTypingDestination(
    onLogout: () -> Unit
) {
    composable(TypingRoute.ROUTE) { backStackEntry ->
        val viewModel: TypingViewModel = hiltViewModel(backStackEntry)
        val appContext = LocalContext.current.applicationContext
        val textMarker: TextMarker = remember {
            EntryPointAccessors.fromApplication(
                appContext,
                TypingUiEntryPoint::class.java
            ).textMarker()
        }
        TypingScreen(
            viewModel = viewModel,
            onChangeUser = onLogout,
            textMarker = textMarker
        )
    }
}
