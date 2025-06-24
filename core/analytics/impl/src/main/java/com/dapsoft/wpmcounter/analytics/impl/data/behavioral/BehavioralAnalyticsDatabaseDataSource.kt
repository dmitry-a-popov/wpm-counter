package com.dapsoft.wpmcounter.analytics.impl.data.behavioral

import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database.AnalyticsDatabase
import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database.KeyEventEntity

import kotlinx.coroutines.flow.Flow

import javax.inject.Inject

internal class BehavioralAnalyticsDatabaseDataSource @Inject constructor(
    private val database: AnalyticsDatabase
) {

    suspend fun saveKeyEventEntity(event: KeyEventEntity) {
        database.keyEventDao().insert(event)
    }

    fun getLatestKeyEventEntity(): Flow<KeyEventEntity?> {
        return database.keyEventDao().getLatestEvent()
    }

    suspend fun deleteAllKeyEventEntities() {
        database.keyEventDao().deleteAllEvents()
    }
}