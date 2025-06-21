package com.dapsoft.wpmcounter.typing.di

import com.dapsoft.wpmcounter.typing.data.SampleTextRepositoryImpl
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
}