package com.dapsoft.wpmcounter.analytics.impl.di

import android.content.Context

import androidx.room.Room

import com.dapsoft.wpmcounter.analytics.ClearEventsUseCase
import com.dapsoft.wpmcounter.analytics.speed.GetTypingSpeedUseCase
import com.dapsoft.wpmcounter.analytics.TrackKeyPressUseCase
import com.dapsoft.wpmcounter.analytics.impl.data.InMemoryTypingSessionStateStore
import com.dapsoft.wpmcounter.analytics.impl.domain.SpeedCalculatorImpl
import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.BehavioralAnalyticsDatabaseDataSource
import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.BehavioralAnalyticsRepositoryImpl
import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database.AnalyticsDatabase
import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.BehavioralAnalyticsDataSource
import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.mapper.KeystrokeEventMapper
import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.mapper.KeystrokeEventMapperImpl
import com.dapsoft.wpmcounter.analytics.impl.domain.BehavioralAnalyticsRepository
import com.dapsoft.wpmcounter.analytics.impl.domain.ClearEventsUseCaseImpl
import com.dapsoft.wpmcounter.analytics.impl.domain.GetTypingSpeedUseCaseImpl
import com.dapsoft.wpmcounter.analytics.impl.domain.SpeedCalculator
import com.dapsoft.wpmcounter.analytics.impl.domain.TrackKeyPressUseCaseImpl
import com.dapsoft.wpmcounter.analytics.impl.domain.TypingSessionStateStore
import com.dapsoft.wpmcounter.analytics.impl.domain.TypingSessionUpdater
import com.dapsoft.wpmcounter.analytics.impl.domain.TypingSessionUpdaterImpl

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {

    @Binds
    internal abstract fun bindBehavioralAnalyticsDataSource(
        impl: BehavioralAnalyticsDatabaseDataSource
    ): BehavioralAnalyticsDataSource

    @Binds
    internal abstract fun bindKeystrokeEventMapper(
        impl: KeystrokeEventMapperImpl
    ) : KeystrokeEventMapper

    @Binds
    internal abstract fun bindBehavioralAnalyticsRepository(
        impl: BehavioralAnalyticsRepositoryImpl
    ): BehavioralAnalyticsRepository

    @Binds
    internal abstract fun bindClearEventsUseCase(
        impl: ClearEventsUseCaseImpl
    ): ClearEventsUseCase

    @Binds
    internal abstract fun bindTrackKeyPressUseCase(
        impl: TrackKeyPressUseCaseImpl
    ): TrackKeyPressUseCase

    @Binds
    internal abstract fun bindTypingSessionStateStore(
        inMemoryTypingSessionStateStore: InMemoryTypingSessionStateStore
    ): TypingSessionStateStore


    @Binds
    internal abstract fun bindGetTypingSpeedUseCase(
        impl: GetTypingSpeedUseCaseImpl
    ): GetTypingSpeedUseCase

    @Binds
    internal abstract fun bindSpeedCalculator(
        impl: SpeedCalculatorImpl
    ): SpeedCalculator

    @Binds
    internal abstract fun bindTypingSessionUpdater(
        impl: TypingSessionUpdaterImpl
    ): TypingSessionUpdater

    companion object {
        @Provides
        @Singleton
        internal fun provideAnalyticsDatabase(
            @ApplicationContext context: Context
        ): AnalyticsDatabase =
            Room.databaseBuilder(
                context,
                AnalyticsDatabase::class.java,
                "analytics_database"
            ).build()
    }
}
