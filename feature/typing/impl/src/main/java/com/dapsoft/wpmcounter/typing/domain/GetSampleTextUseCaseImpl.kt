package com.dapsoft.wpmcounter.typing.domain

internal class GetSampleTextUseCaseImpl(private val sampleTextRepository: SampleTextRepository) : GetSampleTextUseCase {

    override suspend fun invoke() = sampleTextRepository.sampleTextFlow
}