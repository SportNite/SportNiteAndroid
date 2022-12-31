package com.pawlowski.sportnite.di

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.pawlowski.sportnite.*
import com.pawlowski.sportnite.data.auth.AuthManager
import com.pawlowski.sportnite.data.auth.AuthorizationInterceptor
import com.pawlowski.sportnite.data.auth.IAuthManager
import com.pawlowski.sportnite.data.local.*
import com.pawlowski.sportnite.data.mappers.*
import com.pawlowski.sportnite.domain.AppRepository
import com.pawlowski.sportnite.domain.IAppRepository
import com.pawlowski.sportnite.domain.models.MeetingsFilter
import com.pawlowski.sportnite.domain.models.OffersFilter
import com.pawlowski.sportnite.domain.models.PlayersFilter
import com.pawlowski.sportnite.presentation.models.*
import com.pawlowski.sportnite.type.StringOperationFilterInput
import com.pawlowski.sportnite.type.UserFilterInput
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
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
    fun playersStore(apolloClient: ApolloClient, playersInMemoryCache: PlayersInMemoryCache): Store<PlayersFilter, List<Player>> {
        return StoreBuilder.from(fetcher = Fetcher.of { filters: PlayersFilter ->
            apolloClient.query(UsersQuery(filters.toUserFilterInput())).execute().data!!.toPlayersList()!!
        }, sourceOfTruth = SourceOfTruth.of(
            reader = { key: PlayersFilter ->
                playersInMemoryCache.observeData(key)
            },
            writer = { key: PlayersFilter, input: List<Player> ->
                playersInMemoryCache.addManyElements(key, input) {
                    it.uid
                }
            },
            delete = { key: PlayersFilter ->
                playersInMemoryCache.deleteAllElementsWithKey(key)
            },
            deleteAll = { playersInMemoryCache.deleteAllData() }
        )).build()
    }

    @Singleton
    @Provides
    fun offersStore(apolloClient: ApolloClient, offersInMemoryCache: OffersInMemoryCache): Store<OffersFilter, List<GameOffer>> {
        return StoreBuilder.from(fetcher = Fetcher.of { filters: OffersFilter ->
            return@of if(filters.myOffers)
            {
                apolloClient.query(MyOffersQuery()).execute().data!!.toGameOfferList()!!
            }
            else
                apolloClient.query(OffersQuery(offerFilterInput = filters.toOfferFilterInput())).execute().data!!.toGameOfferList()!!
        }, sourceOfTruth = SourceOfTruth.of(
            reader = { key: OffersFilter ->
                offersInMemoryCache.observeData(key)
            },
            writer = { key: OffersFilter, input: List<GameOffer> ->
                offersInMemoryCache.addManyElements(key, input) {
                    it.offerUid
                }
            },
            delete = { key: OffersFilter ->
                offersInMemoryCache.deleteAllElementsWithKey(key)
            },
            deleteAll = { offersInMemoryCache.deleteAllData() }
        )).build()
    }

    @Singleton
    @Provides
    fun offersToAcceptStore(apolloClient: ApolloClient, offersToAcceptMemoryCache: OffersToAcceptMemoryCache): Store<OffersFilter, List<GameOfferToAccept>> {
        return StoreBuilder.from(fetcher = Fetcher.of { filters: OffersFilter ->
            apolloClient.query(ResponsesQuery(filters.sportFilter!!.toSportType())).execute().data!!.toGameOfferToAcceptList()!!
        }, sourceOfTruth = SourceOfTruth.of(
            reader = { key: OffersFilter ->
                offersToAcceptMemoryCache.observeData(key)
            },
            writer = { key: OffersFilter, input: List<GameOfferToAccept> ->
                offersToAcceptMemoryCache.addManyElements(key, input) {
                    it.offerToAcceptUid
                }
            },
            delete = { key: OffersFilter ->
                offersToAcceptMemoryCache.deleteAllElementsWithKey(key)
            },
            deleteAll = { offersToAcceptMemoryCache.deleteAllData() }
        )).build()
    }

    @Singleton
    @Provides
    fun playerDetailsStore(apolloClient: ApolloClient, playerDetailsInMemoryCache: PlayerDetailsInMemoryCache): Store<String, PlayerDetails> {
        return StoreBuilder.from(fetcher = Fetcher.of { filter: String ->
            apolloClient.query(UsersQuery(filter =
            Optional.present(
                UserFilterInput(firebaseUserId = Optional.present(
                    StringOperationFilterInput(
                        eq = Optional.present(filter)
                    )
                ))
            ))).execute().data!!.toPlayerDetails()!!
        }, sourceOfTruth = SourceOfTruth.of(
            reader = { key: String ->
                playerDetailsInMemoryCache.observeData(key).map { it.first() }
            },
            writer = { key: String, input: PlayerDetails ->
                playerDetailsInMemoryCache.addManyElements(key, listOf(input))
                {
                    it.playerUid
                }
            },
            delete = { key: String ->
                playerDetailsInMemoryCache.deleteAllElementsWithKey(key)
            },
            deleteAll = { playerDetailsInMemoryCache.deleteAllData() }
        )).build()
    }


    @Singleton
    @Provides
    fun meetingStore(apolloClient: ApolloClient, meetingsInMemoryCache: MeetingsInMemoryCache, authManager: AuthManager): Store<MeetingsFilter, List<Meeting>> {
        return StoreBuilder.from(fetcher = Fetcher.of { filters: MeetingsFilter ->
            apolloClient.query(
                IncomingOffersQuery(
                    offersFilter = filters.toOfferFilterInput()
                )
            ).execute().data!!.incomingOffers.let { beforeMappingData ->
                val userUid = authManager.getCurrentUserUid()!!
                beforeMappingData.map {
                    it.toMeeting(userUid)
                }
            }
        }, sourceOfTruth = SourceOfTruth.of(
            reader = { key: MeetingsFilter ->
                meetingsInMemoryCache.observeData(key)
            },
            writer = { key: MeetingsFilter, input: List<Meeting> ->
                meetingsInMemoryCache.addManyElements(key, input)
                {
                    it.meetingUid
                }
            },
            delete = { key: MeetingsFilter ->
                meetingsInMemoryCache.deleteAllElementsWithKey(key)
            },
            deleteAll = { meetingsInMemoryCache.deleteAllData() }
        )).build()
    }
}




