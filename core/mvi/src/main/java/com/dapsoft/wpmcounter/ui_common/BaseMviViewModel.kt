package com.dapsoft.wpmcounter.ui_common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Simple base ViewModel implementing a lightweight MVI pattern.
 *
 * Responsibilities:
 * - Holds UI state in a StateFlow.
 * - Serializes intents via a Channel to preserve ordering.
 * - Emits one-time events through a SharedFlow (no replay).
 *
 * Usage:
 * 1. Provide an initial state in the constructor.
 * 2. UI layer calls [dispatch] with domain intents.
 * 3. Subclass implements [reduce] to mutate state via [setState] and send events via [sendSideEffect].
 *
 * Concurrency:
 * - Intents are consumed sequentially in a single coroutine launched in init.
 * - [reduce] is suspend and runs one at a time; no extra synchronization needed for typical mutations.
 *
 * Events:
 * - One-time events use a SharedFlow with extra buffer capacity (64). [sendSideEffect] suspends if buffer full.
 *
 * Lifecycle:
 * - Channel is closed in [onCleared]; queued intents after cancellation are not processed.
 *
 *
 * Generics:
 * - UiState: immutable state holder type.
 * - UiIntent: user/system intent type.
 * - SideEffect: fire-and-forget UI side effect (navigation, toast, etc.).
 */
abstract class BaseMviViewModel<UiState, UiIntent, SideEffect>(initialUiState: UiState) : ViewModel() {

    private val _uiState = MutableStateFlow(initialUiState)
    /**
     * Public immutable state stream for UI consumption.
     */
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<SideEffect>(replay = 0, extraBufferCapacity = 64)
    /**
     * Stream of one-time side effects; collect in a LaunchedEffect.
     */
    val sideEffect: SharedFlow<SideEffect> = _sideEffect.asSharedFlow()

    private val intents = Channel<UiIntent>(Channel.UNLIMITED)

    init {
        viewModelScope.launch {
            for (intent in intents) {
                reduce(intent)
            }
        }
    }

    /**
     * Dispatch an intent; returns immediately.
     */
    fun dispatch(intent: UiIntent) {
        intents.trySend(intent)
    }

    /**
     * Handle a single intent; implement state transitions and side effects.
     */
    protected abstract suspend fun reduce(intent: UiIntent)

    /**
     * Atomically update current state.
     */
    protected fun setState(transform: (UiState) -> UiState) {
        _uiState.value = transform(_uiState.value)
    }

    /**
     * Emit a one-time event; suspends if buffer is full.
     */
    protected suspend fun sendSideEffect(effect: SideEffect) {
        _sideEffect.emit(effect)
    }

    override fun onCleared() {
        intents.close()
        super.onCleared()
    }
}
