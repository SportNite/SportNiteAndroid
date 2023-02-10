package com.pawlowski.auth.di

import com.google.firebase.auth.FirebaseAuth
import com.pawlowski.auth.AuthManager
import com.pawlowski.auth.IAuthManager
import com.pawlowski.auth.ILightAuthManager
import com.pawlowski.auth.LightAuthManager
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
    internal fun lightAuthManager(lightAuthManager: LightAuthManager): ILightAuthManager = lightAuthManager

    @Singleton
    @Provides
    fun firebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
}