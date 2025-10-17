package com.dapsoft.wpmcounter.ui.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController

import com.dapsoft.wpmcounter.ui.AppNavHost

@Composable
fun AppEntry(
    viewModel: AppEntryViewModel = hiltViewModel()
) {
    val startDestination by viewModel.startDestination.collectAsStateWithLifecycle()
    val navController = rememberNavController()

    startDestination.let {
        if (it == null) {
            SplashScreen()
        } else {
            AppNavHost(
                navController = navController,
                startDestination = it
            )
        }
    }
}
