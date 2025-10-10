package com.dapsoft.wpmcounter.analytics.impl.data.behavioral

import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.mapper.KeyEventMapper
import com.dapsoft.wpmcounter.analytics.impl.domain.BehavioralAnalyticsRepository
import com.dapsoft.wpmcounter.analytics.impl.domain.model.KeyEvent
import com.dapsoft.wpmcounter.logger.Logger

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of [BehavioralAnalyticsRepository] that uses a local data source
 * for storing and retrieving behavioral analytics data.
 */
internal class BehavioralAnalyticsRepositoryImpl(
    private val dataSource: BehavioralAnalyticsDataSource,
    private val mapper: KeyEventMapper,
    private val log: Logger
) : BehavioralAnalyticsRepository {

    override suspend fun saveKeyEvent(event: KeyEvent) {
        try {
            dataSource.saveKeyEventEntity(mapper.toEntity(event))
        } catch (e: Exception) {
            log.e(TAG, "Failed to save key event: ${event.keyCode}", e)
        }
    }

    override fun getLatestEvent(): Flow<KeyEvent?> {
        return dataSource.getLatestKeyEventEntity().map { entity ->
            entity?.let { mapper.toDomain(it) }
        }
    }

    override suspend fun deleteAllEvents() {
        try {
            dataSource.deleteAllKeyEventEntities()
        } catch (e: Exception) {
            log.e(TAG, "Failed to delete all key events", e)
        }
    }

    companion object {
        private val TAG = BehavioralAnalyticsRepositoryImpl::class.java.simpleName
    }
}