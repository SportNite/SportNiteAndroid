package com.pawlowski.sportnite.di

import android.app.Activity
import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import com.pawlowski.sportnite.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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


    @Provides
    fun mainActivity(): Activity
    {
        //It's needed because Firebase Auth with phone number needs it...
        return MainActivity.getInstance()!!
    }

    @Provides
    fun ioDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun sharedPreferences(appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences("SportNitePreferences", Context.MODE_PRIVATE)
    }



    @Provides
    fun contentResolver(appContext: Context): ContentResolver
    {
        return appContext.contentResolver
    }


}




