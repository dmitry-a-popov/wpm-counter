package com.dapsoft.wpmcounter.typing.di

import com.dapsoft.wpmcounter.common.validation.TextValidator
import com.dapsoft.wpmcounter.typing.data.StaticSampleTextProvider
import com.dapsoft.wpmcounter.typing.domain.CurrentWordIndicesCalculator
import com.dapsoft.wpmcounter.typing.domain.CurrentWordIndicesCalculatorImpl
import com.dapsoft.wpmcounter.typing.domain.MistakeIndicesCalculator
import com.dapsoft.wpmcounter.typing.domain.MistakeIndicesCalculatorImpl
import com.dapsoft.wpmcounter.typing.domain.SampleTextProvider

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object TypingModule {

    @Provides
    internal fun provideSampleTextRepository(): SampleTextProvider {
        return StaticSampleTextProvider()
    }

    @Provides
    internal fun provideMistakeIndicesCalculator(textValidator: TextValidator): MistakeIndicesCalculator {
        return MistakeIndicesCalculatorImpl(textValidator)
    }

    @Provides
    internal fun provideCurrentWordIndicesCalculator(
    ): CurrentWordIndicesCalculator {
        return CurrentWordIndicesCalculatorImpl()
    }
}
