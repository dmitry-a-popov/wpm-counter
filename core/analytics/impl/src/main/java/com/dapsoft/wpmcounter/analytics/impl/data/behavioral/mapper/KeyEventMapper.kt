package com.dapsoft.wpmcounter.analytics.impl.data.behavioral.mapper

import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database.KeyEventEntity
import com.dapsoft.wpmcounter.analytics.impl.domain.model.KeyEvent

/**
 * Maps between domain KeyEvent and data layer KeyEventEntity objects.
 */
internal interface KeyEventMapper {
    /**
     * Converts a domain model to a database entity.
     */
    fun toEntity(domain: KeyEvent): KeyEventEntity
    /**
     * Converts a database entity to a domain model.
     */
    fun toDomain(entity: KeyEventEntity): KeyEvent
}