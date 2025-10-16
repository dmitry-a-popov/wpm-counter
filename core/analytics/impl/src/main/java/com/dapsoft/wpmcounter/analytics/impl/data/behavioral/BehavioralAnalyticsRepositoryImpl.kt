package com.dapsoft.wpmcounter.analytics.impl.data.behavioral

import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.mapper.KeystrokeEventMapper
import com.dapsoft.wpmcounter.analytics.impl.domain.BehavioralAnalyticsRepository
import com.dapsoft.wpmcounter.analytics.impl.domain.model.KeystrokeEvent
import com.dapsoft.wpmcounter.logger.Logger

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of [BehavioralAnalyticsRepository] that uses a local data source
 * for storing and retrieving behavioral analytics data.
 */
internal class BehavioralAnalyticsRepositoryImpl(
    private val dataSource: BehavioralAnalyticsDataSource,
    private val mapper: KeystrokeEventMapper,
    private val log: Logger
) : BehavioralAnalyticsRepository {

    override suspend fun saveEvent(event: KeystrokeEvent) {
        try {
            dataSource.saveKeystrokeEventEntity(mapper.toEntity(event))
        } catch (e: Exception) {
            log.e(TAG, "Failed to save event: ${event.symbol}", e)
        }
    }

    override fun getLatestEvent(): Flow<KeystrokeEvent?> {
        return dataSource.getLatestKeystrokeEventEntity().map { entity ->
            entity?.let { mapper.toDomain(it) }
        }
    }

    override suspend fun deleteAllEvents() {
        try {
            dataSource.deleteAllKeystrokeEventEntities()
        } catch (e: Exception) {
            log.e(TAG, "Failed to delete all events", e)
        }
    }

    companion object {
        private val TAG = BehavioralAnalyticsRepositoryImpl::class.java.simpleName
    }
}