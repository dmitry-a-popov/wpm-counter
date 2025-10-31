package com.dapsoft.wpmcounter.analytics.impl.data.behavioral.mapper

import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database.KeystrokeEventEntity
import com.dapsoft.wpmcounter.analytics.impl.domain.model.KeystrokeEvent
import com.dapsoft.wpmcounter.common.orientation.ScreenOrientation

import org.junit.Assert.assertEquals
import org.junit.Test

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class KeystrokeEventMapperTest {
    private val mapper = KeystrokeEventMapperImpl()

    @Test
    fun `toEntity maps all fields`() {
        val instant = Instant.fromEpochMilliseconds(123_456_789L)
        val domain = KeystrokeEvent(
            eventTime = instant,
            symbol = 'X',
            screenOrientation = ScreenOrientation.LANDSCAPE,
            userName = "user1"
        )

        val entity = mapper.toEntity(domain)

        assertEquals(0L, entity.id) // always zero (auto-generate)
        assertEquals(instant.toEpochMilliseconds(), entity.eventTimeMillis)
        assertEquals('X', entity.symbol)
        assertEquals(ScreenOrientation.LANDSCAPE, entity.screenOrientation)
        assertEquals("user1", entity.userName)
    }

    @Test
    fun `toDomain maps all fields`() {
        val entity = KeystrokeEventEntity(
            id = 42L,
            eventTimeMillis = 987_654_321L,
            symbol = '#',
            screenOrientation = ScreenOrientation.PORTRAIT,
            userName = "tester"
        )

        val domain = mapper.toDomain(entity)

        assertEquals(987_654_321L, domain.eventTime.toEpochMilliseconds())
        assertEquals('#', domain.symbol)
        assertEquals(ScreenOrientation.PORTRAIT, domain.screenOrientation)
        assertEquals("tester", domain.userName)
    }

    @Test
    fun `round trip preserves domain data for all orientations`() {
        val orientations = listOf(
            ScreenOrientation.PORTRAIT,
            ScreenOrientation.LANDSCAPE,
            ScreenOrientation.UNDEFINED
        )

        orientations.forEach { orientation ->
            val original = KeystrokeEvent(
                eventTime = Instant.fromEpochMilliseconds(1_111_111L + orientation.ordinal),
                symbol = ('A' + orientation.ordinal),
                screenOrientation = orientation,
                userName = "u${orientation.ordinal}"
            )

            val entity = mapper.toEntity(original)
            val reconstructed = mapper.toDomain(entity)

            assertEquals(original.eventTime.toEpochMilliseconds(), reconstructed.eventTime.toEpochMilliseconds())
            assertEquals(original.symbol, reconstructed.symbol)
            assertEquals(original.screenOrientation, reconstructed.screenOrientation)
            assertEquals(original.userName, reconstructed.userName)
        }
    }
}
