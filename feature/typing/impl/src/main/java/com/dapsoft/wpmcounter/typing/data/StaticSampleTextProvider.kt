package com.dapsoft.wpmcounter.typing.data

import com.dapsoft.wpmcounter.typing.domain.SampleTextProvider

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class StaticSampleTextProvider @Inject constructor() : SampleTextProvider {

    override val sampleText: String = """
            He thought he would light the fire when
            he got inside, and make himself some
            breakfast, just to pass away the time;
            but he did not seem able to handle anything
            from a scuttleful of coals to a
        """.trimIndent()
}
