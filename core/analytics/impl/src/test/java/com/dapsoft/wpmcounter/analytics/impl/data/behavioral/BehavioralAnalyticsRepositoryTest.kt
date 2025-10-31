package com.dapsoft.wpmcounter.analytics.impl.data.behavioral

import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database.KeystrokeEventEntity
import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.mapper.KeystrokeEventMapper
import com.dapsoft.wpmcounter.analytics.impl.domain.model.KeystrokeEvent
import com.dapsoft.wpmcounter.common.orientation.ScreenOrientation

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.coEvery

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class BehavioralAnalyticsRepositoryTest {

    private val dataSource: BehavioralAnalyticsDataSource = mockk(relaxed = true)
    private val mapper: KeystrokeEventMapper = mockk(relaxed = true)

    private val repository = BehavioralAnalyticsRepositoryImpl(dataSource, mapper)

    @Test
    fun `saveEvent maps domain and delegates to dataSource`() = runBlocking {
        val instant = Instant.fromEpochMilliseconds(1234L)
        val domain = KeystrokeEvent(
            eventTime = instant,
            symbol = 'A',
            screenOrientation = ScreenOrientation.PORTRAIT,
            userName = "user"
        )
        val entity = KeystrokeEventEntity(
            id = 0L,
            eventTimeMillis = instant.toEpochMilliseconds(),
            symbol = 'A',
            screenOrientation = ScreenOrientation.PORTRAIT,
            userName = "user"
        )

        every { mapper.toEntity(domain) } returns entity

        repository.saveEvent(domain)

        verify(exactly = 1) { mapper.toEntity(domain) }
        coVerify(exactly = 1) { dataSource.saveKeystrokeEventEntity(entity) }
    }

    @Test
    fun `observeLatestEvent maps entity emissions to domain`() = runBlocking {
        val flow = MutableStateFlow<KeystrokeEventEntity?>(null)
        every { dataSource.observeLatestKeystrokeEventEntity() } returns flow

        val observedFlow = repository.observeLatestEvent()

        assertNull(observedFlow.first())
        verify(exactly = 0) { mapper.toDomain(any()) }

        val entity = KeystrokeEventEntity(
            id = 42L,
            eventTimeMillis = 9_999L,
            symbol = '#',
            screenOrientation = ScreenOrientation.LANDSCAPE,
            userName = "tester"
        )
        val domain = KeystrokeEvent(
            eventTime = Instant.fromEpochMilliseconds(9_999L),
            symbol = '#',
            screenOrientation = ScreenOrientation.LANDSCAPE,
            userName = "tester"
        )
        every { mapper.toDomain(entity) } returns domain
        flow.value = entity

        val mapped = observedFlow.first()
        assertEquals(domain.eventTime.toEpochMilliseconds(), mapped?.eventTime?.toEpochMilliseconds())
        assertEquals(domain.symbol, mapped?.symbol)
        assertEquals(domain.screenOrientation, mapped?.screenOrientation)
        assertEquals(domain.userName, mapped?.userName)
        verify(exactly = 1) { mapper.toDomain(entity) }
    }

    @Test
    fun `deleteAllEvents delegates to dataSource`() = runBlocking {
        coEvery { dataSource.deleteAllKeystrokeEventEntities() } returns Unit

        repository.deleteAllEvents()

        coVerify(exactly = 1) { dataSource.deleteAllKeystrokeEventEntities() }
    }
}

