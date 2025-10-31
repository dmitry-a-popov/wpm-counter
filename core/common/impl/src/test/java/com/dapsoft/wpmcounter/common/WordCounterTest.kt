package com.dapsoft.wpmcounter.common

import org.junit.Assert.assertEquals
import org.junit.Test

class WordCounterTest {

    private val counter = WordCounterImpl()

    @Test
    fun `blank text returns zero`() {
        assertEquals(0, counter.count("   \n \t  "))
    }

    @Test
    fun `single word`() {
        assertEquals(1, counter.count("alpha"))
    }

    @Test
    fun `multiple words with mixed whitespace`() {
        assertEquals(3, counter.count(" one   two\tthree  "))
    }

    @Test
    fun `leading and trailing whitespace ignored`() {
        assertEquals(2, counter.count("  a b  "))
    }

    @Test
    fun `punctuation inside word counts as single word`() {
        assertEquals(1, counter.count("hello,world"))
    }

    @Test
    fun `punctuation with whitespace separated words`() {
        assertEquals(2, counter.count("hi, there"))
    }

    @Test
    fun `consecutive whitespace collapsed`() {
        assertEquals(4, counter.count("a  b   c    d"))
    }
}
