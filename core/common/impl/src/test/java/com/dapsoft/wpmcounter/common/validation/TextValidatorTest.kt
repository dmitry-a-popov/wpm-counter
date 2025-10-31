package com.dapsoft.wpmcounter.common.validation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TextValidatorTest {

    private val validator = TextValidatorImpl()

    @Test
    fun `blank typed text returns empty list`() {
        val result = validator.compareWords("sample text", "   \t  \n  ")
        assertTrue(result.isEmpty())
    }

    @Test
    fun `all words match with irregular whitespace`() {
        val sample = "alpha   beta gamma"
        val typed = "  alpha beta   gamma  "
        val result = validator.compareWords(sample, typed)
        assertEquals(3, result.size)
        assertTrue(result.all { it.matches })
    }

    @Test
    fun `typed longer than sample marks extra words non matching`() {
        val sample = "one two"
        val typed = "one two three four"
        val result = validator.compareWords(sample, typed)
        assertEquals(listOf(true, true, false, false), result.map { it.matches })
    }

    @Test
    fun `case sensitivity applied`() {
        val sample = "Hello"
        val typed = "hello"
        val result = validator.compareWords(sample, typed)
        assertEquals(listOf(false), result.map { it.matches })
    }

    @Test
    fun `punctuation considered in matching`() {
        val sample1 = "word,"; val typed1 = "word,"
        val sample2 = "word"; val typed2 = "word,"
        val r1 = validator.compareWords(sample1, typed1)
        val r2 = validator.compareWords(sample2, typed2)
        assertEquals(listOf(true), r1.map { it.matches })
        assertEquals(listOf(false), r2.map { it.matches })
    }

    @Test
    fun `sample blank all typed words non matching`() {
        val result = validator.compareWords("   \n\t  ", "alpha beta")
        assertEquals(listOf(false, false), result.map { it.matches })
    }

    @Test
    fun `partial mismatch only specific words false`() {
        val sample = "one two three"
        val typed = "one twO three"
        val result = validator.compareWords(sample, typed)
        assertEquals(listOf(true, false, true), result.map { it.matches })
    }
}

