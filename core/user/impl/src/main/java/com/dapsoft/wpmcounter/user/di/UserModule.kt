package com.dapsoft.wpmcounter.user.di

import com.dapsoft.wpmcounter.user.SaveUserNameUseCase
import com.dapsoft.wpmcounter.user.data.UserDataStoreDataSource
import com.dapsoft.wpmcounter.user.UserRepository
import com.dapsoft.wpmcounter.user.data.UserRepositoryImpl
import com.dapsoft.wpmcounter.user.domain.SaveUserNameUseCaseImpl

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

    @Provides
    internal fun provideSaveUserNameUseCase(
        userRepository: UserRepository
    ): SaveUserNameUseCase {
        return SaveUserNameUseCaseImpl(userRepository)
    }
}
