package com.dapsoft.wpmcounter.typing.di

import com.dapsoft.wpmcounter.typing.data.SampleTextRepositoryImpl
import com.dapsoft.wpmcounter.typing.domain.CurrentWordIndicesCalculator
import com.dapsoft.wpmcounter.typing.domain.CurrentWordIndicesCalculatorImpl
import com.dapsoft.wpmcounter.typing.domain.MistakeIndicesCalculator
import com.dapsoft.wpmcounter.typing.domain.MistakeIndicesCalculatorImpl
import com.dapsoft.wpmcounter.typing.domain.SampleTextRepository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object TypingModule {

    @Provides
    internal fun provideSampleTextRepository(): SampleTextRepository {
        return SampleTextRepositoryImpl()
    }

    @Provides
    internal fun provideMistakeIndicesCalculator(): MistakeIndicesCalculator {
        return MistakeIndicesCalculatorImpl()
    }

    @Provides
    internal fun provideCurrentWordIndicesCalculator(
    ): CurrentWordIndicesCalculator {
        return CurrentWordIndicesCalculatorImpl()
    }
}
