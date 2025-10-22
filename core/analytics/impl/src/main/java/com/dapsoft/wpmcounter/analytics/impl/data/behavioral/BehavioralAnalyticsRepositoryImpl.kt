package com.dapsoft.wpmcounter.analytics.impl.data.behavioral

import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.mapper.KeystrokeEventMapper
import com.dapsoft.wpmcounter.analytics.impl.domain.BehavioralAnalyticsRepository
import com.dapsoft.wpmcounter.analytics.impl.domain.model.KeystrokeEvent

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [BehavioralAnalyticsRepository] that uses a local data source
 * for storing and retrieving behavioral analytics data.
 */
@Singleton
internal class BehavioralAnalyticsRepositoryImpl @Inject constructor(
    private val dataSource: BehavioralAnalyticsDataSource,
    private val mapper: KeystrokeEventMapper
) : BehavioralAnalyticsRepository {

    override suspend fun saveEvent(event: KeystrokeEvent) {
        dataSource.saveKeystrokeEventEntity(mapper.toEntity(event))
    }

    override fun getLatestEvent(): Flow<KeystrokeEvent?> {
        return dataSource.getLatestKeystrokeEventEntity().map { entity ->
            entity?.let { mapper.toDomain(it) }
        }
    }

    override suspend fun deleteAllEvents() {
        dataSource.deleteAllKeystrokeEventEntities()
    }
}
