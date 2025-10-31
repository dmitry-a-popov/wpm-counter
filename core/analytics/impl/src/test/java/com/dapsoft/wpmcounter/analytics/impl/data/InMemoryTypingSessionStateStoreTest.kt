package com.dapsoft.wpmcounter.analytics.impl.data

import com.dapsoft.wpmcounter.analytics.impl.domain.model.SessionState

import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

@OptIn(ExperimentalTime::class)
class InMemoryTypingSessionStateStoreTest {

    @Test
    fun `initial state is empty`() {
        val store = InMemoryTypingSessionStateStore()
        val state = store.state
        assertNull(state.lastEventTimestamp)
        assertEquals(Duration.ZERO, state.totalActiveTypingTime)
        assertEquals("", state.currentText)
    }

    @Test
    fun `update replaces state with transformed value`() {
        val store = InMemoryTypingSessionStateStore()
        val ts = Instant.fromEpochMilliseconds(12345L)
        store.update { s ->
            s.copy(
                lastEventTimestamp = ts,
                totalActiveTypingTime = s.totalActiveTypingTime + 1500.milliseconds,
                currentText = s.currentText + "abc"
            )
        }
        val state = store.state
        assertEquals(ts, state.lastEventTimestamp)
        assertEquals(1500.milliseconds, state.totalActiveTypingTime)
        assertEquals("abc", state.currentText)
    }

    @Test
    fun `multiple updates accumulate changes`() {
        val store = InMemoryTypingSessionStateStore()
        val ts1 = Instant.fromEpochMilliseconds(10L)
        val ts2 = Instant.fromEpochMilliseconds(20L)
        store.update { it.copy(lastEventTimestamp = ts1, currentText = it.currentText + "a") }
        store.update { it.copy(lastEventTimestamp = ts2, currentText = it.currentText + "b", totalActiveTypingTime = it.totalActiveTypingTime + 200.milliseconds) }
        assertEquals(ts2, store.state.lastEventTimestamp)
        assertEquals("ab", store.state.currentText)
        assertEquals(200.milliseconds, store.state.totalActiveTypingTime)
    }

    @Test
    fun `reset returns to initial state`() {
        val store = InMemoryTypingSessionStateStore()
        store.update { it.copy(lastEventTimestamp = Instant.fromEpochMilliseconds(1L), currentText = "x", totalActiveTypingTime = 50.milliseconds) }
        store.reset()
        assertEquals(SessionState.initial(), store.state)
    }

    @Test
    fun `identity update keeps state unchanged`() {
        val store = InMemoryTypingSessionStateStore()
        val before = store.state
        store.update { it }
        assertEquals(before, store.state)
    }
}

