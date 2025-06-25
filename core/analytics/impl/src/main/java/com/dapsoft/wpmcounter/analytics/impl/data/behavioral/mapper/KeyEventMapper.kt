package com.dapsoft.wpmcounter.analytics.impl.data.behavioral.mapper

import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database.KeyEventEntity
import com.dapsoft.wpmcounter.analytics.impl.domain.model.KeyEvent

internal interface KeyEventMapper {
    fun toEntity(domain: KeyEvent): KeyEventEntity
    fun toDomain(entity: KeyEventEntity): KeyEvent
}