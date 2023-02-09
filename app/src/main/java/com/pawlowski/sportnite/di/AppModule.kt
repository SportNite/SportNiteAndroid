package com.pawlowski.sportnite.di

import android.app.Activity
import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import androidx.work.WorkManager
import com.apollographql.apollo3.ApolloClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.pawlowski.sportnite.*
import com.pawlowski.sportnite.data.auth.AuthorizationInterceptor
import com.pawlowski.sportnite.data.local.*
import com.pawlowski.sportnite.presentation.models.*
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

    @Singleton
    @Provides
    fun firebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun mainActivity(): Activity
    {
        //It's needed because Firebase Auth with phone number needs it...
        return MainActivity.getInstance()!!
    }

    @Singleton
    @Provides
    fun apolloClient(authorizationInterceptor: AuthorizationInterceptor): ApolloClient = ApolloClient.Builder()
        .serverUrl(serverUrl = "https://projektinzynieria.bieszczadywysokie.pl/graphql/")
        .addHttpInterceptor(authorizationInterceptor)
        .build()

    @Provides
    fun ioDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun sharedPreferences(appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences("SportNitePreferences", Context.MODE_PRIVATE)
    }

    @Provides
    fun firebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    fun contentResolver(appContext: Context): ContentResolver
    {
        return appContext.contentResolver
    }

    @Singleton
    @Provides
    fun workManager(appContext: Context): WorkManager {
        return WorkManager.getInstance(appContext)
    }
}




