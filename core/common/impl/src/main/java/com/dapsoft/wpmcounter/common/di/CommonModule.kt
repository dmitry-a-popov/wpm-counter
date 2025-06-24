package com.dapsoft.wpmcounter.common.di

import android.content.Context

import com.dapsoft.wpmcounter.common.ScreenOrientationProviderImpl
import com.dapsoft.wpmcounter.common.SystemTimeProvider
import com.dapsoft.wpmcounter.common.TimeProvider
import com.dapsoft.wpmcounter.common.screenorientation.ScreenOrientationProvider

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Provides
    internal fun provideTimeProvider() : TimeProvider {
        return SystemTimeProvider()
    }

    @Provides
    internal fun provideScreenOrientationProvider(
        @ApplicationContext context: Context
    ): ScreenOrientationProvider {
        return ScreenOrientationProviderImpl(context)
    }
}