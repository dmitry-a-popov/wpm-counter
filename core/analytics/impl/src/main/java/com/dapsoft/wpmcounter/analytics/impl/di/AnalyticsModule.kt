package com.dapsoft.wpmcounter.analytics.impl.di

import android.content.Context
import androidx.room.Room
import com.dapsoft.wpmcounter.analytics.ClearEventsUseCase
import com.dapsoft.wpmcounter.analytics.TrackKeyPressUseCase
import com.dapsoft.wpmcounter.analytics.TrackKeyReleaseUseCase
import com.dapsoft.wpmcounter.analytics.impl.data.BehavioralAnalyticsDatabaseDataSource
import com.dapsoft.wpmcounter.analytics.impl.data.BehavioralAnalyticsRepositoryImpl
import com.dapsoft.wpmcounter.analytics.impl.data.PendingKeyEventsRepositoryImpl
import com.dapsoft.wpmcounter.analytics.impl.data.database.AnalyticsDatabase
import com.dapsoft.wpmcounter.analytics.impl.domain.BehavioralAnalyticsRepository
import com.dapsoft.wpmcounter.analytics.impl.domain.ClearEventUseCaseImpl
import com.dapsoft.wpmcounter.analytics.impl.domain.PendingKeyEventsRepository
import com.dapsoft.wpmcounter.analytics.impl.domain.TrackKeyPressUseCaseImpl
import com.dapsoft.wpmcounter.analytics.impl.domain.TrackKeyReleaseUseCaseImpl
import com.dapsoft.wpmcounter.common.TimeProvider

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
    @Singleton
    internal fun providePendingKeyEventsRepository(): PendingKeyEventsRepository {
        return PendingKeyEventsRepositoryImpl()
    }

    @Provides
    internal fun provideClearEventsUseCase(
        behavioralAnalyticsRepository: BehavioralAnalyticsRepository
    ): ClearEventsUseCase {
        return ClearEventUseCaseImpl(behavioralAnalyticsRepository)
    }

    @Provides
    internal fun provideTrackKeyPressUseCase(
        pendingKeyEventsRepository: PendingKeyEventsRepository,
        timeProvider: TimeProvider
    ): TrackKeyPressUseCase {
        return TrackKeyPressUseCaseImpl(pendingKeyEventsRepository, timeProvider)
    }

    @Provides
    internal fun provideTrackKeyReleaseUseCase(
        pendingKeyEventsRepository: PendingKeyEventsRepository,
        behavioralAnalyticsRepository: BehavioralAnalyticsRepository,
        timeProvider: TimeProvider
    ) : TrackKeyReleaseUseCase {
        return TrackKeyReleaseUseCaseImpl(
            pendingKeyEventsRepository,
            behavioralAnalyticsRepository,
            timeProvider
        )
    }
}