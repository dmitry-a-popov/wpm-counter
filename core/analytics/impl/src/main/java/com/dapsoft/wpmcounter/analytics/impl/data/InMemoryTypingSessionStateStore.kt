package com.dapsoft.wpmcounter.analytics.impl.data

import com.dapsoft.wpmcounter.analytics.impl.domain.TypingSessionStateStore
import com.dapsoft.wpmcounter.analytics.impl.domain.model.SessionState

import javax.inject.Inject
import javax.inject.Singleton

/**
 * In-memory implementation of TypingSessionStateStore.
 */
@Singleton
internal class InMemoryTypingSessionStateStore @Inject constructor() : TypingSessionStateStore {

    private var _state: SessionState = SessionState()

    override var state: SessionState
        get() {
            return _state
        }
        set(value) {
            _state = value
        }

    override fun reset() {
        _state = SessionState()
    }

}
