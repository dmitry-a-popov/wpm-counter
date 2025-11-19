package com.dapsoft.wpmcounter.typing.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class CurrentWordIndicesCalculatorTest {

    private lateinit var calculator: CurrentWordIndicesCalculator

    @Before
    fun setUp() {
        calculator = CurrentWordIndicesCalculatorImpl()
    }

    @Test
    fun `returns null when text is empty`() {
        val text = ""

        val result = calculator.calculate(text, wordNumber = 0)

        assertNull(result)
    }

    @Test
    fun `returns indices of the requested word`() {
        val text = "  jump high run"

        val result = calculator.calculate(text, wordNumber = 1)

        assertEquals(7 until 11, result)
    }

    @Test
    fun `returns null when word number is out of bounds`() {
        val text = "alpha beta"

        val result = calculator.calculate(text, wordNumber = 5)

        assertNull(result)
    }

    @Test
    fun `handles multiple whitespace characters between words`() {
        val text = "fly\tfast\nsteady"

        val result = calculator.calculate(text, wordNumber = 2)

        assertEquals(9 until 15, result)
    }
}


