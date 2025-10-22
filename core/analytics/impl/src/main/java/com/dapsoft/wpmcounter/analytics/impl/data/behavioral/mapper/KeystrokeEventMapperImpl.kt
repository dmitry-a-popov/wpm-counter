package com.dapsoft.wpmcounter.analytics.impl.data.behavioral.mapper

import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database.KeystrokeEventEntity
import com.dapsoft.wpmcounter.analytics.impl.domain.model.KeystrokeEvent

import javax.inject.Inject

import kotlin.time.Duration.Companion.milliseconds

/**
 * Implements mapping between KeystrokeEvent domain models and KeystrokeEventEntity data objects.
 */
internal class KeystrokeEventMapperImpl @Inject constructor() : KeystrokeEventMapper {

    override fun toEntity(domain: KeystrokeEvent): KeystrokeEventEntity {
        return KeystrokeEventEntity(
            id = 0, // Auto-generated
            eventTimeMillis = domain.eventTime.inWholeMilliseconds,
            symbol = domain.symbol,
            screenOrientation = domain.screenOrientation,
            userName = domain.userName
        )
    }

    override fun toDomain(entity: KeystrokeEventEntity): KeystrokeEvent {
        return KeystrokeEvent(
            eventTime = entity.eventTimeMillis.milliseconds,
            symbol = entity.symbol,
            screenOrientation = entity.screenOrientation,
            userName = entity.userName
        )
    }
}
