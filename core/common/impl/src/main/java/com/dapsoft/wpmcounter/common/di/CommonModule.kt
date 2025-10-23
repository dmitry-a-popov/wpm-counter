package com.dapsoft.wpmcounter.common.di

import android.content.Context

import com.dapsoft.wpmcounter.common.ScreenOrientationProviderImpl
import com.dapsoft.wpmcounter.common.SystemTimeProvider
import com.dapsoft.wpmcounter.common.TimeProvider
import com.dapsoft.wpmcounter.common.WordCounter
import com.dapsoft.wpmcounter.common.WordCounterImpl
import com.dapsoft.wpmcounter.common.orientation.ScreenOrientationProvider
import com.dapsoft.wpmcounter.common.validation.TextValidator
import com.dapsoft.wpmcounter.common.validation.TextValidatorImpl

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

    @Provides
    internal fun provideTextValidator(): TextValidator {
        return TextValidatorImpl()
    }

    @Provides
    internal fun provideWordCounter(): WordCounter {
        return WordCounterImpl()
    }
}
