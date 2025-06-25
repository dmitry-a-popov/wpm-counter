package com.dapsoft.wpmcounter.common.di

import android.content.Context

import com.dapsoft.wpmcounter.common.ScreenOrientationProviderImpl
import com.dapsoft.wpmcounter.common.SystemTimeProvider
import com.dapsoft.wpmcounter.common.TimeProvider
import com.dapsoft.wpmcounter.common.orientation.ScreenOrientationProvider
import com.dapsoft.wpmcounter.common.validation.TextValidator
import com.dapsoft.wpmcounter.common.validation.TextValidatorImpl
import com.dapsoft.wpmcounter.common.validation.WordValidator
import com.dapsoft.wpmcounter.common.validation.WordValidatorImpl
import com.dapsoft.wpmcounter.logger.Logger

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
    internal fun provideWordValidator(log: Logger): WordValidator {
        return WordValidatorImpl(log)
    }

    @Provides
    internal fun provideTextValidator(wordValidator: WordValidator): TextValidator {
        return TextValidatorImpl(wordValidator)
    }
}