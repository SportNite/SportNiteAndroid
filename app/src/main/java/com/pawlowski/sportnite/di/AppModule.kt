package com.pawlowski.sportnite.di

import android.app.Application
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.pawlowski.sportnite.MainActivity
import com.pawlowski.sportnite.data.auth.AuthManager
import com.pawlowski.sportnite.data.auth.IAuthManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun appContext(app: Application): Context
    {
        return app.applicationContext
    }

    @Singleton
    @Provides
    fun firebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun mainActivity(): MainActivity
    {
        //It's needed because Firebase Auth with phone number needs it...
        return MainActivity.getInstance()!!
    }

    @Singleton
    @Provides
    fun authManager(authManager: AuthManager): IAuthManager = authManager
}