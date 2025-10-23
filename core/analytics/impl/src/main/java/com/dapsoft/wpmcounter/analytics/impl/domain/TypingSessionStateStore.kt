package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.analytics.impl.domain.model.SessionState

/**
 * Holder of the current typing session state.
 * Invariant: all mutations should go through [update] or [reset]
 */
internal interface TypingSessionStateStore {
    val state: SessionState
    fun update(transform: (SessionState) -> SessionState)
    fun reset()
}
