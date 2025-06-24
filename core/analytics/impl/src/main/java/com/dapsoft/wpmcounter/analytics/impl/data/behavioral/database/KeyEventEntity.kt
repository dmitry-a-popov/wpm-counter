package com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database

import androidx.room.Entity
import androidx.room.PrimaryKey

import com.dapsoft.wpmcounter.common.screenorientation.ScreenOrientation

@Entity(tableName = "key_events")
internal data class KeyEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val keyPressTime: Long,
    val keyReleaseTime: Long,
    val keyCode: Char,
    val phoneOrientation: ScreenOrientation,
    val username: String,
    val timestamp: Long
)
