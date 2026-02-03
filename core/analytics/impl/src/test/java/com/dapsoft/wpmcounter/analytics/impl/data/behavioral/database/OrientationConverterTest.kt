package com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database

import com.dapsoft.wpmcounter.common.orientation.ScreenOrientation
import org.junit.Assert
import org.junit.Test

class OrientationConverterTest {

    private val converter = OrientationConverter()

    @Test
    fun `fromOrientation returns underlying code`() {
        ScreenOrientation.entries.forEach { orientation ->
            Assert.assertEquals(orientation.code, converter.fromOrientation(orientation))
        }
    }

    @Test
    fun `toOrientation maps known values`() {
        ScreenOrientation.entries.forEach { orientation ->
            Assert.assertEquals(orientation, converter.toOrientation(orientation.code))
        }
    }

    @Test
    fun `toOrientation maps unknown value to UNDEFINED`() {
        Assert.assertEquals(ScreenOrientation.UNDEFINED, converter.toOrientation("unknown_value"))
    }

    @Test
    fun `round trip preserves orientation for defined values`() {
        ScreenOrientation.entries.forEach { orientation ->
            val stored = converter.fromOrientation(orientation)
            val restored = converter.toOrientation(stored)
            Assert.assertEquals(orientation, restored)
        }
    }
}
