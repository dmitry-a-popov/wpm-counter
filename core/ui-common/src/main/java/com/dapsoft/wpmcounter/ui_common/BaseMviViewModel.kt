package com.dapsoft.wpmcounter.ui_common

import androidx.lifecycle.ViewModel

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseMviViewModel<UiState, UiIntent, OneTimeEvent>(private val initialUiState: UiState) : ViewModel() {

    protected val _uiState = MutableStateFlow<UiState>(initialUiState)
    val uiState: StateFlow<UiState> = _uiState

    protected val _oneTimeEvent = OneTimeEventFlow<OneTimeEvent>()
    val oneTimeEvent: SharedFlow<OneTimeEvent> = _oneTimeEvent.events

    abstract fun processIntent(intent: UiIntent): Job
}