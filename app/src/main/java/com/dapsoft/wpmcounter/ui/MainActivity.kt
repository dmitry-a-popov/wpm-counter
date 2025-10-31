package com.dapsoft.wpmcounter.ui

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import com.dapsoft.wpmcounter.ui.root.AppEntry
import com.dapsoft.wpmcounter.ui.theme.AppTheme

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                AppEntry()
            }
        }
    }
}
