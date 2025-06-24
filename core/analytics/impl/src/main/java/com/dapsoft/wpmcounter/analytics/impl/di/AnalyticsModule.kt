package com.dapsoft.wpmcounter.analytics.impl.di

import android.content.Context

import androidx.room.Room

import com.dapsoft.wpmcounter.analytics.ClearEventsUseCase
import com.dapsoft.wpmcounter.analytics.GetTypingSpeedUseCase
import com.dapsoft.wpmcounter.analytics.TrackKeyPressUseCase
import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.BehavioralAnalyticsDatabaseDataSource
import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.BehavioralAnalyticsRepositoryImpl
import com.dapsoft.wpmcounter.analytics.impl.data.behavioral.database.AnalyticsDatabase
import com.dapsoft.wpmcounter.analytics.impl.data.TypingSpeedRepositoryImpl
import com.dapsoft.wpmcounter.analytics.impl.domain.BehavioralAnalyticsRepository
import com.dapsoft.wpmcounter.analytics.impl.domain.ClearEventUseCaseImpl
import com.dapsoft.wpmcounter.analytics.impl.domain.GetTypingSpeedUseCaseImpl
import com.dapsoft.wpmcounter.analytics.impl.domain.TrackKeyPressUseCaseImpl
import com.dapsoft.wpmcounter.analytics.impl.domain.TypingSpeedRepository
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
    internal fun provideBehavioralAnalyticsRepository(
        dataSource: BehavioralAnalyticsDatabaseDataSource
    ): BehavioralAnalyticsRepository {
        return BehavioralAnalyticsRepositoryImpl(dataSource)
    }

    @Provides
    internal fun provideClearEventsUseCase(
        behavioralAnalyticsRepository: BehavioralAnalyticsRepository
    ): ClearEventsUseCase {
        return ClearEventUseCaseImpl(behavioralAnalyticsRepository)
    }

    @Provides
    internal fun provideTrackKeyPressUseCase(
        behavioralAnalyticsRepository: BehavioralAnalyticsRepository
    ): TrackKeyPressUseCase {
        return TrackKeyPressUseCaseImpl(behavioralAnalyticsRepository)
    }

    @Provides
    @Singleton
    internal fun provideWordsRepository() : TypingSpeedRepository {
        return TypingSpeedRepositoryImpl()
    }

    @Provides
    internal fun provideGetTypingSpeedUseCase(
        analyticsRepository: BehavioralAnalyticsRepository,
        typingSpeedRepository: TypingSpeedRepository,
        log: Logger
    ): GetTypingSpeedUseCase {
        return GetTypingSpeedUseCaseImpl(analyticsRepository, typingSpeedRepository, log)
    }
}