package com.dapsoft.wpmcounter.user.di

import com.dapsoft.wpmcounter.user.UserDataStoreDataSource
import com.dapsoft.wpmcounter.user.UserRepository
import com.dapsoft.wpmcounter.user.UserRepositoryImpl

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    internal fun provideUserRepository(
        userDataStoreDataSource: UserDataStoreDataSource
    ): UserRepository {
        return UserRepositoryImpl(userDataStoreDataSource)
    }
}