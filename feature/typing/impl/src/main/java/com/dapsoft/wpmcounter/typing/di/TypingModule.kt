package com.dapsoft.wpmcounter.typing.di

import com.dapsoft.wpmcounter.typing.data.SampleTextRepositoryImpl
import com.dapsoft.wpmcounter.typing.domain.CountWordUseCase
import com.dapsoft.wpmcounter.typing.domain.CountWordUseCaseImpl
import com.dapsoft.wpmcounter.typing.domain.GetCurrentWordIndicesUseCase
import com.dapsoft.wpmcounter.typing.domain.GetCurrentWordIndicesUseCaseImpl
import com.dapsoft.wpmcounter.typing.domain.GetMistakeIndicesUseCase
import com.dapsoft.wpmcounter.typing.domain.GetMistakeIndicesUseCaseImpl
import com.dapsoft.wpmcounter.typing.domain.GetSampleTextUseCase
import com.dapsoft.wpmcounter.typing.domain.GetSampleTextUseCaseImpl
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
    internal fun provideGetSampleTextUseCase(
        sampleTextRepository: SampleTextRepository
    ): GetSampleTextUseCase {
        return GetSampleTextUseCaseImpl(sampleTextRepository)
    }

    @Provides
    internal fun provideGetMistakeIndicesUseCase(
        getSampleTextUseCase: GetSampleTextUseCase
    ): GetMistakeIndicesUseCase {
        return GetMistakeIndicesUseCaseImpl()
    }

    @Provides
    internal fun provideCountWordsUseCase(): CountWordUseCase {
        return CountWordUseCaseImpl()
    }

    @Provides
    internal fun provideGetCurrentWordIndicesUseCase(
    ): GetCurrentWordIndicesUseCase {
        return GetCurrentWordIndicesUseCaseImpl()
    }
}