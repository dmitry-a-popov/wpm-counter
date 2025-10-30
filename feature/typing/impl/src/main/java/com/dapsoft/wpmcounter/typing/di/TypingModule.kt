package com.dapsoft.wpmcounter.typing.di

import com.dapsoft.wpmcounter.typing.data.StaticSampleTextProvider
import com.dapsoft.wpmcounter.typing.domain.CurrentWordIndicesCalculator
import com.dapsoft.wpmcounter.typing.domain.CurrentWordIndicesCalculatorImpl
import com.dapsoft.wpmcounter.typing.domain.MistakeIndicesCalculator
import com.dapsoft.wpmcounter.typing.domain.MistakeIndicesCalculatorImpl
import com.dapsoft.wpmcounter.typing.domain.SampleTextProvider
import com.dapsoft.wpmcounter.typing.ui.TextMarkerConfig

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TypingModule {

    @Binds
    internal abstract fun bindSampleTextRepository(impl: StaticSampleTextProvider): SampleTextProvider

    @Binds
    internal abstract fun bindMistakeIndicesCalculator(impl: MistakeIndicesCalculatorImpl): MistakeIndicesCalculator

    @Binds
    internal abstract fun bindCurrentWordIndicesCalculator(
        impl: CurrentWordIndicesCalculatorImpl
    ): CurrentWordIndicesCalculator

    companion object {
        @Provides
        fun provideTextMarkerConfig(): TextMarkerConfig = TextMarkerConfig()
    }
}
