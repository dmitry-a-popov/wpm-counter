package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.logger.LogLevel
import com.dapsoft.wpmcounter.logger.Logger

import io.mockk.mockk
import io.mockk.verify
import io.mockk.slot

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class SpeedCalculatorTest {

    private val delta = 0.0001f

    @Test
    fun `negative words count throws`() {
        val logger: Logger = mockk(relaxed = true)
        val calculator = SpeedCalculatorImpl(logger)
        assertThrows(IllegalArgumentException::class.java) {
            calculator.calculateWordsPerMinute(-1, 1.minutes)
        }
        verify(exactly = 0) { logger.log(any(), any(), any(), any()) }
    }

    @Test
    fun `negative active time throws`() {
        val logger: Logger = mockk(relaxed = true)
        val calculator = SpeedCalculatorImpl(logger)
        assertThrows(IllegalArgumentException::class.java) {
            calculator.calculateWordsPerMinute(1, (-1).seconds)
        }
        verify(exactly = 0) { logger.log(any(), any(), any(), any()) }
    }

    @Test
    fun `zero words returns 0 and logs`() {
        val logger: Logger = mockk(relaxed = true)
        val calculator = SpeedCalculatorImpl(logger)
        val tag = SpeedCalculatorImpl::class.java.simpleName
        val wpm = calculator.calculateWordsPerMinute(0, 2.minutes)
        assertEquals(0f, wpm, delta)
        verify(exactly = 1) { logger.log(LogLevel.DEBUG, tag, null, any()) }
    }

    @Test
    fun `zero active time returns 0 and logs`() {
        val logger: Logger = mockk(relaxed = true)
        val calculator = SpeedCalculatorImpl(logger)
        val tag = SpeedCalculatorImpl::class.java.simpleName
        val wpm = calculator.calculateWordsPerMinute(5, Duration.ZERO)
        assertEquals(0f, wpm, delta)
        verify(exactly = 1) { logger.log(LogLevel.DEBUG, tag, null, any()) }
    }

    @Test
    fun `active time below threshold returns 0 and logs`() {
        val logger: Logger = mockk(relaxed = true)
        val calculator = SpeedCalculatorImpl(logger)
        val tag = SpeedCalculatorImpl::class.java.simpleName
        val wpm = calculator.calculateWordsPerMinute(3, 59.milliseconds)
        assertEquals(0f, wpm, delta)
        verify(exactly = 1) { logger.log(LogLevel.DEBUG, tag, null, any()) }
    }

    @Test
    fun `active time slightly above threshold calculates speed`() {
        val logger: Logger = mockk(relaxed = true)
        val calculator = SpeedCalculatorImpl(logger)
        val tag = SpeedCalculatorImpl::class.java.simpleName
        val active = 61.milliseconds
        val expected = 2f / (active.inWholeMilliseconds.toFloat() / 1.minutes.inWholeMilliseconds.toFloat())
        val wpm = calculator.calculateWordsPerMinute(2, active)
        assertEquals(expected, wpm, delta)
        verify(exactly = 1) { logger.log(LogLevel.DEBUG, tag, null, any()) }
    }

    @Test
    fun `regular calculation returns correct ratio and logs`() {
        val logger: Logger = mockk(relaxed = true)
        val calculator = SpeedCalculatorImpl(logger)
        val tag = SpeedCalculatorImpl::class.java.simpleName
        val wpm = calculator.calculateWordsPerMinute(5, 2.minutes)
        assertEquals(2.5f, wpm, delta)
        verify(exactly = 1) { logger.log(LogLevel.DEBUG, tag, null, any()) }
    }

    @Test
    fun `high words count single minute`() {
        val logger: Logger = mockk(relaxed = true)
        val calculator = SpeedCalculatorImpl(logger)
        val tag = SpeedCalculatorImpl::class.java.simpleName
        val wpm = calculator.calculateWordsPerMinute(120, 1.minutes)
        assertEquals(120f, wpm, delta)
        verify(exactly = 1) { logger.log(LogLevel.DEBUG, tag, null, any()) }
    }

    @Test
    fun `multiple calls produce separate logs`() {
        val logger: Logger = mockk(relaxed = true)
        val calculator = SpeedCalculatorImpl(logger)
        val tag = SpeedCalculatorImpl::class.java.simpleName
        calculator.calculateWordsPerMinute(1, 1.minutes)
        calculator.calculateWordsPerMinute(2, 2.minutes)
        calculator.calculateWordsPerMinute(0, 5.minutes)
        verify(exactly = 3) { logger.log(LogLevel.DEBUG, tag, null, any()) }
    }

    @Test
    fun `message lambda includes result for calculation path`() {
        val logger: Logger = mockk(relaxed = true)
        val calculator = SpeedCalculatorImpl(logger)
        val tag = SpeedCalculatorImpl::class.java.simpleName
        val captured = slot<() -> String>()
        calculator.calculateWordsPerMinute(10, 5.minutes)
        verify { logger.log(LogLevel.DEBUG, tag, null, capture(captured)) }
        val message = captured.captured.invoke()
        assert(message.contains("result=2.0"))
    }
}
