package com.dapsoft.wpmcounter.typing.data

import com.dapsoft.wpmcounter.typing.domain.SampleTextRepository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class SampleTextRepositoryImpl : SampleTextRepository {

    override val text: Flow<String>
        get() = flowOf(SAMPLE_TEXT)

    companion object {
        private val SAMPLE_TEXT = """
            He thought he would light the fire when
            he got inside, and make himself some
            breakfast, just to pass away the time;
            but he did not seem able to handle anything
            from a scuttleful of coals to a
        """.trimIndent()
    }
}
