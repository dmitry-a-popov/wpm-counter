package com.dapsoft.wpmcounter.logger.di

import android.content.Context
import android.content.pm.ApplicationInfo

import com.dapsoft.wpmcounter.logger.LogLevel
import com.dapsoft.wpmcounter.logger.Logger
import com.dapsoft.wpmcounter.logger.LoggerImpl

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LoggerModule {

    @Provides
    internal fun provideLogger(
        @ApplicationContext context: Context
    ): Logger {
        val debuggable = (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        val minLevel = if (debuggable) LogLevel.VERBOSE else LogLevel.WARN
        return LoggerImpl(minLevel)
    }
}
