package com.dapsoft.wpmcounter.analytics.impl.data.behavioral.mapper

import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database.KeyEventEntity
import com.dapsoft.wpmcounter.analytics.impl.domain.model.KeyEvent
import kotlin.time.Duration.Companion.milliseconds

/**
 * Implements mapping between KeyEvent domain models and KeyEventEntity data objects.
 */
internal class KeyEventMapperImpl : KeyEventMapper {

    override fun toEntity(domain: KeyEvent): KeyEventEntity {
        return KeyEventEntity(
            id = 0, // Auto-generated
            eventTimeMillis = domain.eventTime.inWholeMilliseconds,
            symbol = domain.symbol,
            phoneOrientation = domain.phoneOrientation,
            username = domain.username
        )
    }

    override fun toDomain(entity: KeyEventEntity): KeyEvent {
        return KeyEvent(
            eventTime = entity.eventTimeMillis.milliseconds,
            symbol = entity.symbol,
            phoneOrientation = entity.phoneOrientation,
            username = entity.username
        )
    }
}