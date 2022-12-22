package com.pawlowski.sportnite.di

import android.app.Application
import android.content.Context
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.http.HttpHeader
import com.google.firebase.auth.FirebaseAuth
import com.pawlowski.sportnite.MainActivity
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
        .httpHeaders(listOf(HttpHeader("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6ImFmZjFlNDJlNDE0M2I4MTQxM2VjMTI1MzQwOTcwODUxZThiNDdiM2YiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vc3BvcnRuaXRlLTdiMDcwIiwiYXVkIjoic3BvcnRuaXRlLTdiMDcwIiwiYXV0aF90aW1lIjoxNjcxNjQ3NTQwLCJ1c2VyX2lkIjoiSTdLeWtnTFFLeFRrdHdhZ1Nmb2wwaWFST0hHMiIsInN1YiI6Ikk3S3lrZ0xRS3hUa3R3YWdTZm9sMGlhUk9IRzIiLCJpYXQiOjE2NzE2NDc1NDAsImV4cCI6MTY3MTY1MTE0MCwiZW1haWwiOiJtYWNpZWtwYXdsb3dza2kxQG9uZXQucGwiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsiZW1haWwiOlsibWFjaWVrcGF3bG93c2tpMUBvbmV0LnBsIl19LCJzaWduX2luX3Byb3ZpZGVyIjoicGFzc3dvcmQifX0.r9t7s8RJIv7qCeXTJ71Mk2g22XyBbTA7X7G2PMiizeJOdljO6-bvyzX607p2haaCjamHcUILBPoslvdrTGezoKBElI7GPhDfv7P8magso_9ftTdU6VC6YtjZ7ixQjQSsDxc1wLniGifnMNHTbMHFAKEqlDttKXJp-8jAG81ZgI_z2teur8wjOrXlRAf9Dv_sa8MCHmwnVlqCSpY7qHpD4l_zvOorDQYsHtsD2UVWAnM4qpAmFTOo2ILowX6I5nXH3h60vojKkj2p5K_3HEFw7jgoOz_aMAC1eNGnfL7F6Q05bgG_doU5cSbbRB_W-igelidBh80O748avyPbdntZVg")))
        .build()

    @Provides
    fun ioDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun appRepository(appRepository: AppRepository): IAppRepository = appRepository


}