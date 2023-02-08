package com.pawlowski.di

import com.pawlowski.auth.AuthManager
import com.pawlowski.auth.IAuthManager
import com.pawlowski.auth.ILightAuthManager
import com.pawlowski.auth.LightAuthManager
import com.pawlowski.cache.IUserInfoUpdateCache
import com.pawlowski.cache.UserInfoUpdateCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Singleton
    @Provides
    internal fun authManager(authManager: AuthManager): IAuthManager = authManager

    @Singleton
    @Provides
    internal fun userInfoUpdateCache(userInfoUpdateCache: UserInfoUpdateCache): IUserInfoUpdateCache = userInfoUpdateCache

    @Singleton
    @Provides
    internal fun lightAuthManager(lightAuthManager: LightAuthManager): ILightAuthManager = lightAuthManager
}