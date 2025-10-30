package com.dapsoft.wpmcounter.typing.di

import com.dapsoft.wpmcounter.typing.data.StaticSampleTextProvider
import com.dapsoft.wpmcounter.typing.domain.CurrentWordIndicesCalculator
import com.dapsoft.wpmcounter.typing.domain.CurrentWordIndicesCalculatorImpl
import com.dapsoft.wpmcounter.typing.domain.MistakeIndicesCalculator
import com.dapsoft.wpmcounter.typing.domain.MistakeIndicesCalculatorImpl
import com.dapsoft.wpmcounter.typing.domain.SampleTextProvider

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TypingModule {

    @Binds
    internal abstract fun provideSampleTextRepository(impl: StaticSampleTextProvider): SampleTextProvider

    @Binds
    internal abstract fun provideMistakeIndicesCalculator(impl: MistakeIndicesCalculatorImpl): MistakeIndicesCalculator

    @Binds
    internal abstract fun provideCurrentWordIndicesCalculator(
        impl: CurrentWordIndicesCalculatorImpl
    ): CurrentWordIndicesCalculator
}
