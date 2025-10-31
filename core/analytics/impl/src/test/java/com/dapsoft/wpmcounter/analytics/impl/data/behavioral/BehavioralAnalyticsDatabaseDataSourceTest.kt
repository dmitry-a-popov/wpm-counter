package com.dapsoft.wpmcounter.analytics.impl.data.behavioral

import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database.AnalyticsDatabase
import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database.KeystrokeEventDao
import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database.KeystrokeEventEntity
import com.dapsoft.wpmcounter.common.orientation.ScreenOrientation

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.coEvery

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

import org.junit.Assert.assertEquals
import org.junit.Test

class BehavioralAnalyticsDatabaseDataSourceTest {

    private val dao: KeystrokeEventDao = mockk(relaxed = true)
    private val database: AnalyticsDatabase = mockk {
        every { keystrokeEventDao() } returns dao
    }

    private val dataSource = BehavioralAnalyticsDatabaseDataSource(database)

    @Test
    fun `saveKeystrokeEventEntity delegates insert to dao`() = runBlocking {
        val entity = KeystrokeEventEntity(
            id = 0L,
            eventTimeMillis = 123L,
            symbol = 'A',
            screenOrientation = ScreenOrientation.PORTRAIT,
            userName = "user"
        )

        dataSource.saveKeystrokeEventEntity(entity)

        coVerify(exactly = 1) { dao.insert(entity) }
    }

    @Test
    fun `observeLatestKeystrokeEventEntity returns flow from dao`() = runBlocking {
        val flow = MutableStateFlow<KeystrokeEventEntity?>(null)
        every { dao.observeLatestEvent() } returns flow

        val observed = dataSource.observeLatestKeystrokeEventEntity()
        assertEquals(null, observed.first())

        val entity = KeystrokeEventEntity(
            id = 42L,
            eventTimeMillis = 999L,
            symbol = '#',
            screenOrientation = ScreenOrientation.LANDSCAPE,
            userName = "tester"
        )
        flow.value = entity

        assertEquals(entity, observed.first())
    }

    @Test
    fun `deleteAllKeystrokeEventEntities delegates to dao`() = runBlocking {
        coEvery { dao.deleteAllEvents() } returns Unit

        dataSource.deleteAllKeystrokeEventEntities()

        coVerify(exactly = 1) { dao.deleteAllEvents() }
    }
}

