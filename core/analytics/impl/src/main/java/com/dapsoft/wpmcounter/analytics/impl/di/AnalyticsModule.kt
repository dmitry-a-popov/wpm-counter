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
import com.dapsoft.wpmcounter.analytics.impl.domain.ClearEventUseCaseImpl
import com.dapsoft.wpmcounter.analytics.impl.domain.GetTypingSpeedUseCaseImpl
import com.dapsoft.wpmcounter.analytics.impl.domain.SpeedCalculator
import com.dapsoft.wpmcounter.analytics.impl.domain.TrackKeyPressUseCaseImpl
import com.dapsoft.wpmcounter.analytics.impl.domain.TypingSessionStateStore
import com.dapsoft.wpmcounter.analytics.impl.domain.TypingSessionUpdater
import com.dapsoft.wpmcounter.analytics.impl.domain.TypingSessionUpdaterImpl
import com.dapsoft.wpmcounter.common.TimeProvider
import com.dapsoft.wpmcounter.common.orientation.ScreenOrientationProvider
import com.dapsoft.wpmcounter.logger.Logger

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {

    @Provides
    @Singleton
    internal fun provideAnalyticsDatabase(@ApplicationContext context: Context): AnalyticsDatabase {
        return Room.databaseBuilder(
            context,
            AnalyticsDatabase::class.java,
            "analytics_database"
        ).build()
    }

    @Provides
    internal fun provideBehavioralAnalyticsDataSource(
        database: AnalyticsDatabase
    ): BehavioralAnalyticsDataSource {
        return BehavioralAnalyticsDatabaseDataSource(database)
    }

    @Provides
    internal fun provideKeystrokeEventMapper() : KeystrokeEventMapper {
        return KeystrokeEventMapperImpl()
    }

    @Provides
    internal fun provideBehavioralAnalyticsRepository(
        dataSource: BehavioralAnalyticsDataSource,
        mapper: KeystrokeEventMapper,
        log: Logger
    ): BehavioralAnalyticsRepository {
        return BehavioralAnalyticsRepositoryImpl(dataSource, mapper, log)
    }

    @Provides
    internal fun provideClearEventsUseCase(
        behavioralAnalyticsRepository: BehavioralAnalyticsRepository,
        typingSessionStateStore: TypingSessionStateStore
    ): ClearEventsUseCase {
        return ClearEventUseCaseImpl(behavioralAnalyticsRepository, typingSessionStateStore)
    }

    @Provides
    internal fun provideTrackKeyPressUseCase(
        behavioralAnalyticsRepository: BehavioralAnalyticsRepository,
        screenOrientationProvider: ScreenOrientationProvider,
        timeProvider: TimeProvider
    ): TrackKeyPressUseCase {
        return TrackKeyPressUseCaseImpl(
            behavioralAnalyticsRepository,
            screenOrientationProvider,
            timeProvider
        )
    }

    @Provides
    @Singleton
    internal fun provideTypingSessionStateStore(): TypingSessionStateStore {
        return InMemoryTypingSessionStateStore()
    }

    @Provides
    internal fun provideGetTypingSpeedUseCase(
        analyticsRepo: BehavioralAnalyticsRepository,
        speedCalculator: SpeedCalculator,
        typingSessionUpdater: TypingSessionUpdater,
        log: Logger
    ): GetTypingSpeedUseCase {
        return GetTypingSpeedUseCaseImpl(
            analyticsRepo,
            speedCalculator,
            typingSessionUpdater,
            log
        )
    }

    @Provides
    internal fun provideSpeedCalculator(log: Logger): SpeedCalculator {
        return SpeedCalculatorImpl(log)
    }

    @Provides
    internal fun provideTypingSessionUpdater(typingSessionStateStore: TypingSessionStateStore): TypingSessionUpdater {
        return TypingSessionUpdaterImpl(typingSessionStateStore)

    }
}
