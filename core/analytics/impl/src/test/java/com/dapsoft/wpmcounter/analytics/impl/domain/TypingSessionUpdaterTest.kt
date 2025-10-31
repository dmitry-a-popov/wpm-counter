package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.analytics.impl.domain.model.SessionState
import com.dapsoft.wpmcounter.analytics.impl.data.InMemoryTypingSessionStateStore

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class TypingSessionUpdaterTest {

    private val store = InMemoryTypingSessionStateStore()
    private val updater = TypingSessionUpdaterImpl(store)

    @Test
    fun `first keystroke sets timestamp keeps active time zero`() {
        val t1 = Instant.fromEpochMilliseconds(1000)
        val state = updater.updateForKeystroke('a', t1, 100.milliseconds)
        assertEquals(t1, state.lastEventTimestamp)
        assertEquals(0.milliseconds, state.totalActiveTypingTime)
        assertEquals("a", state.currentText)
    }

    @Test
    fun `second keystroke within threshold accumulates active time`() {
        val threshold = 200.milliseconds
        val t1 = Instant.fromEpochMilliseconds(1000)
        val t2 = Instant.fromEpochMilliseconds(1150)
        updater.updateForKeystroke('a', t1, threshold)
        val state2 = updater.updateForKeystroke('b', t2, threshold)
        assertEquals(t2, state2.lastEventTimestamp)
        assertEquals((t2 - t1), state2.totalActiveTypingTime)
        assertEquals("ab", state2.currentText)
    }

    @Test
    fun `second keystroke at threshold does not accumulate active time`() {
        val threshold = 150.milliseconds
        val t1 = Instant.fromEpochMilliseconds(2000)
        val t2 = Instant.fromEpochMilliseconds(2150) // diff == threshold
        updater.updateForKeystroke('x', t1, threshold)
        val state2 = updater.updateForKeystroke('y', t2, threshold)
        assertEquals(0.milliseconds, state2.totalActiveTypingTime)
        assertEquals("xy", state2.currentText)
    }

    @Test
    fun `mixed sequence accumulates only within-threshold intervals`() {
        val threshold = 100.milliseconds
        val t1 = Instant.fromEpochMilliseconds(3000)
        val t2 = Instant.fromEpochMilliseconds(3050) // +50 (add)
        val t3 = Instant.fromEpochMilliseconds(3200) // +150 (skip)
        val t4 = Instant.fromEpochMilliseconds(3250) // +50 (add)
        updater.updateForKeystroke('a', t1, threshold)
        updater.updateForKeystroke('b', t2, threshold)
        updater.updateForKeystroke('c', t3, threshold)
        val state4 = updater.updateForKeystroke('d', t4, threshold)
        val expectedActive = (t2 - t1) + (t4 - t3)
        assertEquals(expectedActive, state4.totalActiveTypingTime)
        assertEquals("abcd", state4.currentText)
        assertEquals(t4, state4.lastEventTimestamp)
    }

    @Test
    fun `zero pause threshold throws`() {
        val t = Instant.fromEpochMilliseconds(4000)
        assertThrows(IllegalArgumentException::class.java) {
            updater.updateForKeystroke('z', t, 0.milliseconds)
        }
        assertEquals(SessionState.initial(), store.state)
    }

    @Test
    fun `returned state matches store state after update`() {
        val threshold = 120.milliseconds
        val t1 = Instant.fromEpochMilliseconds(5000)
        val t2 = Instant.fromEpochMilliseconds(5070)
        updater.updateForKeystroke('q', t1, threshold)
        val s2 = updater.updateForKeystroke('w', t2, threshold)
        assertEquals(store.state, s2)
        assertEquals((t2 - t1), s2.totalActiveTypingTime)
        assertEquals("qw", s2.currentText)
    }
}

