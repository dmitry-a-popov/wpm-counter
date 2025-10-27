package com.dapsoft.wpmcounter.common

/**
 * Counts words in the provided text.
 */
interface WordCounter {

    fun count(text: String) : Int
}
