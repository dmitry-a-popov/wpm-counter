package com.dapsoft.wpmcounter.ui_common

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class OneTimeEventFlow<T> {
    private val _events = MutableSharedFlow<T>(extraBufferCapacity = 1)
    val events: SharedFlow<T> = _events.asSharedFlow()

    suspend fun emit(value: T) {
        _events.emit(value)
    }
}