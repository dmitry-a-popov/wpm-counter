package com.dapsoft.wpmcounter.common.di

import com.dapsoft.wpmcounter.common.SystemTimeProvider
import com.dapsoft.wpmcounter.common.TimeProvider

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Provides
    internal fun provideTimeProvider() : TimeProvider {
        return SystemTimeProvider()
    }

}