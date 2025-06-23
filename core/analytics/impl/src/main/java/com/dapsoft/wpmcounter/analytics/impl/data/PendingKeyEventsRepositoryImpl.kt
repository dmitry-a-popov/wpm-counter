package com.dapsoft.wpmcounter.analytics.impl.data

import com.dapsoft.wpmcounter.analytics.impl.domain.PendingKeyEventsRepository
import com.dapsoft.wpmcounter.analytics.impl.domain.model.KeyEvent

internal class PendingKeyEventsRepositoryImpl : PendingKeyEventsRepository {

    private val pendingKeyEvents = mutableMapOf<Char, KeyEvent>()

    override fun savePendingKeyEvent(pendingEvent: KeyEvent) {
        pendingKeyEvents[pendingEvent.keyCode] = pendingEvent
    }

    override fun getAndRemovePendingKeyEvent(keyCode: Char): KeyEvent? {
        return pendingKeyEvents.remove(keyCode)
    }
}