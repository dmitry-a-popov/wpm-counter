package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.analytics.impl.domain.model.KeyEvent

internal interface PendingKeyEventsRepository {

    fun savePendingKeyEvent(pendingEvent: KeyEvent)

    fun getAndRemovePendingKeyEvent(keyCode: Char): KeyEvent?
}