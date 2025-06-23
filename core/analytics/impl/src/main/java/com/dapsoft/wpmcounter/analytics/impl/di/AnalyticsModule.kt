package com.dapsoft.wpmcounter.analytics.impl.di

import com.dapsoft.wpmcounter.analytics.ClearEventsUseCase
import com.dapsoft.wpmcounter.analytics.TrackKeyPressUseCase
import com.dapsoft.wpmcounter.analytics.TrackKeyReleaseUseCase
import com.dapsoft.wpmcounter.analytics.impl.data.BehavioralAnalyticsDatabaseDataSource
import com.dapsoft.wpmcounter.analytics.impl.data.BehavioralAnalyticsRepositoryImpl
import com.dapsoft.wpmcounter.analytics.impl.data.PendingKeyEventsRepositoryImpl
import com.dapsoft.wpmcounter.analytics.impl.domain.BehavioralAnalyticsRepository
import com.dapsoft.wpmcounter.analytics.impl.domain.ClearEventUseCaseImpl
import com.dapsoft.wpmcounter.analytics.impl.domain.PendingKeyEventsRepository
import com.dapsoft.wpmcounter.analytics.impl.domain.TrackKeyPressUseCaseImpl
import com.dapsoft.wpmcounter.analytics.impl.domain.TrackKeyReleaseUseCaseImpl
import com.dapsoft.wpmcounter.common.TimeProvider

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {

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