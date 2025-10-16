package com.dapsoft.wpmcounter.analytics.impl.data.behavioral.mapper

import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database.KeystrokeEventEntity
import com.dapsoft.wpmcounter.analytics.impl.domain.model.KeystrokeEvent

import kotlin.time.Duration.Companion.milliseconds

/**
 * Implements mapping between KeystrokeEvent domain models and KeystrokeEventEntity data objects.
 */
internal class KeystrokeEventMapperImpl : KeystrokeEventMapper {

    override fun toEntity(domain: KeystrokeEvent): KeystrokeEventEntity {
        return KeystrokeEventEntity(
            id = 0, // Auto-generated
            eventTimeMillis = domain.eventTime.inWholeMilliseconds,
            symbol = domain.symbol,
            phoneOrientation = domain.phoneOrientation,
            username = domain.username
        )
    }

    override fun toDomain(entity: KeystrokeEventEntity): KeystrokeEvent {
        return KeystrokeEvent(
            eventTime = entity.eventTimeMillis.milliseconds,
            symbol = entity.symbol,
            phoneOrientation = entity.phoneOrientation,
            username = entity.username
        )
    }
}