package com.pawlowski.di

import com.pawlowski.auth.AuthManager
import com.pawlowski.auth.IAuthManager
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
}