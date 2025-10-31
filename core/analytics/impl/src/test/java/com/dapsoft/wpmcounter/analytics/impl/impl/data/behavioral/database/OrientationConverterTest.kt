package com.dapsoft.wpmcounter.analytics.impl.impl.data.behavioral.database

import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database.OrientationConverter
import com.dapsoft.wpmcounter.common.orientation.ScreenOrientation

import org.junit.Assert.assertEquals
import org.junit.Test

class OrientationConverterTest {

    private val converter = OrientationConverter()

    @Test
    fun `toInt returns underlying value`() {
        assertEquals(ScreenOrientation.PORTRAIT.value, converter.toInt(ScreenOrientation.PORTRAIT))
        assertEquals(ScreenOrientation.LANDSCAPE.value, converter.toInt(ScreenOrientation.LANDSCAPE))
        assertEquals(ScreenOrientation.UNDEFINED.value, converter.toInt(ScreenOrientation.UNDEFINED))
    }

    @Test
    fun `toOrientation maps known values`() {
        assertEquals(ScreenOrientation.PORTRAIT, converter.toOrientation(ScreenOrientation.PORTRAIT.value))
        assertEquals(ScreenOrientation.LANDSCAPE, converter.toOrientation(ScreenOrientation.LANDSCAPE.value))
        assertEquals(ScreenOrientation.UNDEFINED, converter.toOrientation(ScreenOrientation.UNDEFINED.value))
    }

    @Test
    fun `toOrientation maps unknown value to UNDEFINED`() {
        assertEquals(ScreenOrientation.UNDEFINED, converter.toOrientation(42))
    }

    @Test
    fun `round trip preserves orientation for defined values`() {
        ScreenOrientation.entries.forEach { orientation ->
            val stored = converter.toInt(orientation)
            val restored = converter.toOrientation(stored)
            assertEquals(orientation, restored)
        }
    }
}
