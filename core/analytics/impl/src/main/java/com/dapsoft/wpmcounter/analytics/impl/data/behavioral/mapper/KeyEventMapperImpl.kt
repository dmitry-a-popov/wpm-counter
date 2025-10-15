package com.dapsoft.wpmcounter.analytics.impl.data.behavioral.mapper

import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database.KeyEventEntity
import com.dapsoft.wpmcounter.analytics.impl.domain.model.KeyEvent

/**
 * Implements mapping between KeyEvent domain models and KeyEventEntity data objects.
 */
internal class KeyEventMapperImpl : KeyEventMapper {

    override fun toEntity(domain: KeyEvent): KeyEventEntity {
        return KeyEventEntity(
            id = 0, // Auto-generated
            eventTimeMillis = domain.eventTimeMillis,
            symbol = domain.symbol,
            phoneOrientation = domain.phoneOrientation,
            username = domain.username
        )
    }

    override fun toDomain(entity: KeyEventEntity): KeyEvent {
        return KeyEvent(
            eventTimeMillis = entity.eventTimeMillis,
            symbol = entity.symbol,
            phoneOrientation = entity.phoneOrientation,
            username = entity.username
        )
    }
}