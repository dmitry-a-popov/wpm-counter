package com.dapsoft.wpmcounter.analytics.impl.data.behavioral

import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database.KeyEventEntity
import com.dapsoft.wpmcounter.analytics.impl.domain.BehavioralAnalyticsRepository
import com.dapsoft.wpmcounter.analytics.impl.domain.model.KeyEvent

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class BehavioralAnalyticsRepositoryImpl(
    private val dataSource: BehavioralAnalyticsDatabaseDataSource
) : BehavioralAnalyticsRepository {

    override suspend fun saveKeyEvent(event: KeyEvent) {
        dataSource.saveKeyEventEntity(event.toEntity())
    }

    override fun getLatestEvent(): Flow<KeyEvent?> {
        return dataSource.getLatestKeyEventEntity().map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun deleteAllEvents() {
        dataSource.deleteAllKeyEventEntities()
    }

    private fun KeyEvent.toEntity(): KeyEventEntity {
        return KeyEventEntity(
            id = 0, // Auto-generated
            keyPressTime = keyPressTime,
            keyReleaseTime = keyReleaseTime,
            keyCode = keyCode,
            phoneOrientation = phoneOrientation,
            username = username,
            timestamp = System.currentTimeMillis()
        )
    }

    private fun KeyEventEntity.toDomain(): KeyEvent {
        return KeyEvent(
            keyPressTime = keyPressTime,
            keyReleaseTime = keyReleaseTime,
            keyCode = keyCode,
            phoneOrientation = phoneOrientation,
            username = username
        )
    }

}