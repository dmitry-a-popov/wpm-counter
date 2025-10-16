package com.dapsoft.wpmcounter.analytics.impl.data.behavioral.mapper

import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database.KeystrokeEventEntity
import com.dapsoft.wpmcounter.analytics.impl.domain.model.KeystrokeEvent

/**
 * Maps between domain KeystrokeEvent and data layer KeystrokeEventEntity objects.
 */
internal interface KeystrokeEventMapper {
    /**
     * Converts a domain model to a database entity.
     */
    fun toEntity(domain: KeystrokeEvent): KeystrokeEventEntity
    /**
     * Converts a database entity to a domain model.
     */
    fun toDomain(entity: KeystrokeEventEntity): KeystrokeEvent
}