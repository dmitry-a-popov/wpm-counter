package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.analytics.impl.domain.model.SessionState

internal interface TypingSessionStateStore {
    var state: SessionState
    fun reset()
}