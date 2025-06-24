package com.dapsoft.wpmcounter.logger.di

import com.dapsoft.wpmcounter.logger.Logger
import com.dapsoft.wpmcounter.logger.LoggerImpl

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LoggerModule {

    @Provides
    internal fun provideLogger(): Logger {
        return LoggerImpl()
    }
}