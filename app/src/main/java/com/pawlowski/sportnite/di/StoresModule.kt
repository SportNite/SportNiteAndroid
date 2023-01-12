package com.pawlowski.sportnite.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.pawlowski.sportnite.*
import com.pawlowski.sportnite.data.auth.AuthManager
import com.pawlowski.sportnite.data.local.*
import com.pawlowski.sportnite.data.mappers.*
import com.pawlowski.sportnite.domain.models.MeetingsFilter
import com.pawlowski.sportnite.domain.models.OffersFilter
import com.pawlowski.sportnite.domain.models.PlayersFilter
import com.pawlowski.sportnite.presentation.models.*
import com.pawlowski.sportnite.type.OfferFilterInput
import com.pawlowski.sportnite.type.SportTypeOperationFilterInput
import com.pawlowski.sportnite.type.StringOperationFilterInput
import com.pawlowski.sportnite.type.UserFilterInput
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class StoresModule {
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
                apolloClient.query(
                    MyOffersQuery(
                    filters = Optional.present(
                        listOf(
                            futureOffersOfferFilterInput()
                        )
                    )
                )
                ).execute().data!!.toGameOfferList()!!
            }
            else
                apolloClient.query(OffersQuery(offerFilterInput = filters.toOfferFilterInput(), first = Optional.present(10))).execute().data!!.toGameOfferList()!!
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
            apolloClient.query(
                ResponsesQuery(
                    first = Optional.present(50),
                    otherFilters = filters.sportFilter?.let {
                        Optional.present(listOf(
                            OfferFilterInput(sport = Optional.present(SportTypeOperationFilterInput(eq = Optional.present(it.toSportType()))))
                        ))
                    }?:Optional.absent()
                )
            ).execute().dataAssertNoErrors.toGameOfferToAcceptList()!!

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
            apolloClient.query(
                UsersQuery(filter =
            Optional.present(
                UserFilterInput(firebaseUserId = Optional.present(
                    StringOperationFilterInput(
                        eq = Optional.present(filter)
                    )
                ))
            ))
            ).execute().data!!.toPlayerDetails()!!
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