package com.dapsoft.wpmcounter.analytics.impl.domain

import com.dapsoft.wpmcounter.analytics.impl.domain.model.KeyEvent

import kotlinx.coroutines.flow.Flow

internal interface BehavioralAnalyticsRepository {

    suspend fun saveKeyEvent(event: KeyEvent)

    fun getLatestEvent(): Flow<KeyEvent?>

    suspend fun deleteAllEvents()
}