package com.dapsoft.wpmcounter.analytics.impl.data.behavioral.mapper

import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database.KeystrokeEventEntity
import com.dapsoft.wpmcounter.analytics.impl.domain.model.KeystrokeEvent

import javax.inject.Inject

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Implements mapping between KeystrokeEvent domain models and KeystrokeEventEntity data objects.
 */
internal class KeystrokeEventMapperImpl @Inject constructor() : KeystrokeEventMapper {

    @ExperimentalTime
    override fun toEntity(domain: KeystrokeEvent): KeystrokeEventEntity {
        return KeystrokeEventEntity(
            id = 0, // Auto-generated
            eventTimeMillis = domain.eventTime.toEpochMilliseconds(),
            symbol = domain.symbol,
            screenOrientation = domain.screenOrientation,
            userName = domain.userName
        )
    }

    @ExperimentalTime
    override fun toDomain(entity: KeystrokeEventEntity): KeystrokeEvent {
        return KeystrokeEvent(
            eventTime = Instant.fromEpochMilliseconds(entity.eventTimeMillis),
            symbol = entity.symbol,
            screenOrientation = entity.screenOrientation,
            userName = entity.userName
        )
    }
}
