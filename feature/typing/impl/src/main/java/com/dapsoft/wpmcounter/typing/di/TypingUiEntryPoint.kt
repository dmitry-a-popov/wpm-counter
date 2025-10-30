package com.dapsoft.wpmcounter.typing.di

import com.dapsoft.wpmcounter.typing.ui.TextMarker

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface TypingUiEntryPoint {
    fun textMarker(): TextMarker
}
