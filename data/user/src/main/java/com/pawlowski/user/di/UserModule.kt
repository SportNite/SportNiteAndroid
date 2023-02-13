package com.pawlowski.user.di

import com.pawlowski.user.IUserRepository
import com.pawlowski.user.UserRepository
import com.pawlowski.user.data.IUserInfoUpdateCache
import com.pawlowski.user.data.UserInfoUpdateCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Singleton
    @Provides
    internal fun userRepository(userRepository: UserRepository): IUserRepository = userRepository


    @Singleton
    @Provides
    internal fun userInfoUpdateCache(userInfoUpdateCache: UserInfoUpdateCache): IUserInfoUpdateCache = userInfoUpdateCache
}