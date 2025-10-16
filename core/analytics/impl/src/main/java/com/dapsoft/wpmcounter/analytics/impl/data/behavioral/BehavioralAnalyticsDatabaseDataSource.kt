package com.dapsoft.wpmcounter.analytics.impl.data.behavioral

import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database.AnalyticsDatabase
import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database.KeystrokeEventEntity

import kotlinx.coroutines.flow.Flow

/**
 * Database implementation of [BehavioralAnalyticsDataSource] using Room.
 */
internal class BehavioralAnalyticsDatabaseDataSource (
    private val database: AnalyticsDatabase
) : BehavioralAnalyticsDataSource {

    override suspend fun saveKeystrokeEventEntity(event: KeystrokeEventEntity) {
        database.keystrokeEventDao().insert(event)
    }

    override fun getLatestKeystrokeEventEntity(): Flow<KeystrokeEventEntity?> {
        return database.keystrokeEventDao().getLatestEvent()
    }

    override suspend fun deleteAllKeystrokeEventEntities() {
        database.keystrokeEventDao().deleteAllEvents()
    }
}