package com.dapsoft.wpmcounter.analytics.impl.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "key_events")
data class KeyEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val keyPressTime: Long,
    val keyReleaseTime: Long,
    val keyCode: Char,
    val phoneOrientation: Int,
    val username: String,
    val timestamp: Long
)
