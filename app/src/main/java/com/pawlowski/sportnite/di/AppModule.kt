package com.pawlowski.sportnite.di

import android.app.Application
import android.content.Context
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.http.HttpHeader
import com.google.firebase.auth.FirebaseAuth
import com.pawlowski.sportnite.MainActivity
import com.pawlowski.sportnite.data.auth.AuthManager
import com.pawlowski.sportnite.data.auth.AuthorizationInterceptor
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
    fun apolloClient(authorizationInterceptor: AuthorizationInterceptor): ApolloClient = ApolloClient.Builder()
        .serverUrl(serverUrl = "https://projektinzynieria.bieszczadywysokie.pl/graphql/")
        .addHttpInterceptor(authorizationInterceptor)
        //.httpHeaders(listOf(HttpHeader("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6ImFmZjFlNDJlNDE0M2I4MTQxM2VjMTI1MzQwOTcwODUxZThiNDdiM2YiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vc3BvcnRuaXRlLTdiMDcwIiwiYXVkIjoic3BvcnRuaXRlLTdiMDcwIiwiYXV0aF90aW1lIjoxNjcxNzQ0MDM3LCJ1c2VyX2lkIjoiSTdLeWtnTFFLeFRrdHdhZ1Nmb2wwaWFST0hHMiIsInN1YiI6Ikk3S3lrZ0xRS3hUa3R3YWdTZm9sMGlhUk9IRzIiLCJpYXQiOjE2NzE3NDQwMzcsImV4cCI6MTY3MTc0NzYzNywiZW1haWwiOiJtYWNpZWtwYXdsb3dza2kxQG9uZXQucGwiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsiZW1haWwiOlsibWFjaWVrcGF3bG93c2tpMUBvbmV0LnBsIl19LCJzaWduX2luX3Byb3ZpZGVyIjoicGFzc3dvcmQifX0.FRij7sdasv3or6Q4bS_EZJ8BRI-7TpDDPt6jAfRgVONr9IvG-r_JLJsybQ9_vqN7HUCjzuKEc_zTwO-ltULqiOdo_iu8quTZ3VbVoOIoUyRPVZsxpZ61T22Aaz0WzeYjA7uGNYJ47tbbvAAWJ_0OywGoqccTQFfF3UUG280a-V9rt-dxHeWaRatsLaTzPW1O7sNSZvSBQFIzzx446hR4MTh5BXq7Ue6W9kzLya7LK6FBHQl5dswTDbTorRRHQ7TYIJb910K3x9z9cubXhLRvvB9uWHXwMobF2WUl0FtN5YTKhKK7YC-ZJwvYwkkNkMbahZkz-oZMMj3J1Qlh6uQ4Iw")))
        .build()

    @Provides
    fun ioDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun appRepository(appRepository: AppRepository): IAppRepository = appRepository


}