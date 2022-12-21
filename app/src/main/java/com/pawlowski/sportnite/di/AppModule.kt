package com.pawlowski.sportnite.di

import android.app.Application
import android.content.Context
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.http.HttpHeader
import com.google.firebase.auth.FirebaseAuth
import com.pawlowski.sportnite.MainActivity
import com.pawlowski.sportnite.MeQuery
import com.pawlowski.sportnite.VersionQuery
import com.pawlowski.sportnite.data.auth.AuthManager
import com.pawlowski.sportnite.data.auth.IAuthManager
import com.pawlowski.sportnite.domain.AppRepository
import com.pawlowski.sportnite.domain.IAppRepository
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
    fun mainActivity(): MainActivity
    {
        //It's needed because Firebase Auth with phone number needs it...
        return MainActivity.getInstance()!!
    }

    @Singleton
    @Provides
    fun authManager(authManager: AuthManager): IAuthManager = authManager

    @Singleton
    @Provides
    fun apolloClient(): ApolloClient = ApolloClient.Builder()
        .serverUrl(serverUrl = "https://projektinzynieria.bieszczadywysokie.pl/graphql/")
        .httpHeaders(listOf(HttpHeader("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6ImFmZjFlNDJlNDE0M2I4MTQxM2VjMTI1MzQwOTcwODUxZThiNDdiM2YiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vc3BvcnRuaXRlLTdiMDcwIiwiYXVkIjoic3BvcnRuaXRlLTdiMDcwIiwiYXV0aF90aW1lIjoxNjcxNDU3OTI0LCJ1c2VyX2lkIjoiSTdLeWtnTFFLeFRrdHdhZ1Nmb2wwaWFST0hHMiIsInN1YiI6Ikk3S3lrZ0xRS3hUa3R3YWdTZm9sMGlhUk9IRzIiLCJpYXQiOjE2NzE0NTc5MjQsImV4cCI6MTY3MTQ2MTUyNCwiZW1haWwiOiJtYWNpZWtwYXdsb3dza2kxQG9uZXQucGwiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsiZW1haWwiOlsibWFjaWVrcGF3bG93c2tpMUBvbmV0LnBsIl19LCJzaWduX2luX3Byb3ZpZGVyIjoicGFzc3dvcmQifX0.enEzmCuRi6GrizM3vwuYvgnwAKfd9457RlnTaVmWkOH5m9m5PXoxw1LouLp2hVLtwdMR7drHWU7b_qvUB__vzqdqz1gdTuWjykKWnkbyo117nfqpHfyPsaksKTIj7azPcZFY9TJDx9xHTJrYGbJzFPYIeeJAIhbTTCxcagA0NwoqZs00XgONZrNPsAKlUfQAoDAxxBpt069BEezRM7OzM_Mg2zQ5H9yYjHK2VSHPa24wicbso2yc4ff_1YuSHQ5j_3eemCCXw2ZDAkEGMwVovehYdg04Ct9UkJLZTWSNjgoNHcoSWSY30YBXBIRh8KDlSdLs6mQUt330AqJA3s3ZBg")))
        .build()

    @Provides
    fun ioDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun appRepository(appRepository: AppRepository): IAppRepository = appRepository
}