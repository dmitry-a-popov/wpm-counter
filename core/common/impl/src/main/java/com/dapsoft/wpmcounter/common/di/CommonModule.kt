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

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CommonModule {

    @Binds
    internal abstract fun bindTimeProvider(impl: SystemTimeProvider) : TimeProvider

    @Binds
    internal abstract fun bindScreenOrientationProvider(impl: ScreenOrientationProviderImpl): ScreenOrientationProvider

    @Binds
    internal abstract fun provideTextValidator(impl: TextValidatorImpl): TextValidator

    @Binds
    internal abstract fun provideWordCounter(impl: WordCounterImpl): WordCounter
}
