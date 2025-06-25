package com.dapsoft.wpmcounter.analytics.impl.data.behavioral.mapper

import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database.KeyEventEntity
import com.dapsoft.wpmcounter.analytics.impl.domain.model.KeyEvent

internal class KeyEventMapperImpl : KeyEventMapper {

    override fun toEntity(domain: KeyEvent): KeyEventEntity {
        return KeyEventEntity(
            id = 0, // Auto-generated
            keyPressTime = domain.keyPressTime,
            keyReleaseTime = domain.keyReleaseTime,
            keyCode = domain.keyCode,
            phoneOrientation = domain.phoneOrientation,
            username = domain.username
        )
    }

    override fun toDomain(entity: KeyEventEntity): KeyEvent {
        return KeyEvent(
            keyPressTime = entity.keyPressTime,
            keyReleaseTime = entity.keyReleaseTime,
            keyCode = entity.keyCode,
            phoneOrientation = entity.phoneOrientation,
            username = entity.username
        )
    }
}