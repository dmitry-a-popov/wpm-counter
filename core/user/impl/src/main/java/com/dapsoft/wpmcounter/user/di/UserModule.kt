package com.dapsoft.wpmcounter.user.di

import com.dapsoft.wpmcounter.user.SaveUserNameUseCase
import com.dapsoft.wpmcounter.user.data.UserDataStoreDataSource
import com.dapsoft.wpmcounter.user.UserRepository
import com.dapsoft.wpmcounter.user.data.UserRepositoryImpl
import com.dapsoft.wpmcounter.user.domain.SaveUserNameUseCaseImpl
import dagger.Binds

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UserModule {

    @Binds
    internal abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

    @Binds
    internal abstract fun bindSaveUserNameUseCase(
        impl: SaveUserNameUseCaseImpl
    ): SaveUserNameUseCase
}
