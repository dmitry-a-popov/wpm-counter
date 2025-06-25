package com.dapsoft.wpmcounter.analytics.impl.data.behavioral

import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database.AnalyticsDatabase
import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database.KeyEventEntity

import kotlinx.coroutines.flow.Flow

/**
 * Database implementation of [BehavioralAnalyticsDataSource] using Room.
 */
internal class BehavioralAnalyticsDatabaseDataSource (
    private val database: AnalyticsDatabase
) : BehavioralAnalyticsDataSource {

    override suspend fun saveKeyEventEntity(event: KeyEventEntity) {
        database.keyEventDao().insert(event)
    }

    override fun getLatestKeyEventEntity(): Flow<KeyEventEntity?> {
        return database.keyEventDao().getLatestEvent()
    }

    override suspend fun deleteAllKeyEventEntities() {
        database.keyEventDao().deleteAllEvents()
    }
}