package com.dapsoft.wpmcounter.typing.domain

import com.dapsoft.wpmcounter.common.validation.TextValidator
import com.dapsoft.wpmcounter.common.validation.WordComparison

import io.mockk.every
import io.mockk.mockk

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MistakeIndicesCalculatorTest {

    private lateinit var textValidator: TextValidator
    private lateinit var calculator: MistakeIndicesCalculator

    @Before
    fun setUp() {
        textValidator = mockk()
        calculator = MistakeIndicesCalculatorImpl(textValidator)
    }

    @Test
    fun `returns indices of every mismatched word`() {
        val sampleText = "red blue green"
        val typedText = "red blue green"
        every { textValidator.compareWords(sampleText, typedText) } returns listOf(
            WordComparison(matches = true),
            WordComparison(matches = false),
            WordComparison(matches = false)
        )

        val result = calculator.calculate(sampleText, typedText)

        assertEquals(listOf(4 until 8, 9 until 14), result)
    }

    @Test
    fun `handles leading trailing and repeated whitespaces`() {
        val sampleText = "go fast stop"
        val typedText = " go  fast   stop "
        every { textValidator.compareWords(sampleText, typedText) } returns listOf(
            WordComparison(matches = false),
            WordComparison(matches = true),
            WordComparison(matches = false)
        )

        val result = calculator.calculate(sampleText, typedText)

        assertEquals(listOf(1 until 3, 12 until 16), result)
    }

    @Test
    fun `ignores extra typed words when validator returns fewer comparisons`() {
        val sampleText = "alpha beta gamma"
        val typedText = "alpha beta gamma"
        every { textValidator.compareWords(sampleText, typedText) } returns listOf(
            WordComparison(matches = false),
            WordComparison(matches = true)
        )

        val result = calculator.calculate(sampleText, typedText)

        assertEquals(listOf(0 until 5), result)
    }
}

