package com.pawlowski.network.data

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.api.Optional
import com.pawlowski.models.*
import com.pawlowski.models.params_models.*
import com.pawlowski.network.*
import com.pawlowski.network.mappers.*
import com.pawlowski.network.type.*
import com.pawlowski.utils.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.time.OffsetDateTime
import javax.inject.Inject

internal class GraphQLService @Inject constructor(
    private val apolloClient: ApolloClient,
    private val ioDispatcher: CoroutineDispatcher,
) : IGraphQLService {

    override suspend fun getOffers(
        filters: OffersFilter,
        cursor: String?,
        pageSize: Int
    ): Resource<PaginationPage<GameOffer>> {
        return when(filters.myOffers) {
            false -> {
                executeApolloQuery(
                    request = {
                        apolloClient.query(
                            OffersQuery(
                                offerFilterInput = filters.toOfferFilterInput(),
                                after = Optional.presentIfNotNull(cursor),
                                first = Optional.present(pageSize)
                            )
                        ).execute()
                    },
                    mapper = { data ->
                        val pageInfo = data.offers?.pageInfo!!
                        PaginationPage(data = data.toGameOfferList()!!, hasNextPage = pageInfo.hasNextPage, endCursor = pageInfo.endCursor)
                    }
                )
            }
            true -> {
                executeApolloQuery(
                    request = {
                        apolloClient.query(
                            MyOffersQuery(
                                filters = filters.toOfferFilterInput(),
                                after = Optional.presentIfNotNull(cursor),
                                first = Optional.present(pageSize)
                            )
                        ).execute()
                    },
                    mapper = { data ->
                        val pageInfo = data.myOffers?.pageInfo!!
                        PaginationPage(data = data.toGameOfferList()!!, hasNextPage = pageInfo.hasNextPage, endCursor = pageInfo.endCursor)
                    }
                )
            }
        }
    }

    override suspend fun getPlayers(
        filters: PlayersFilter,
        cursor: String?,
        pageSize: Int
    ): Resource<PaginationPage<Player>> {
        return executeApolloQuery(
            request = {
                apolloClient.query(UsersQuery(filters.toUserFilterInput(), first = Optional.present(pageSize), cursor = Optional.presentIfNotNull(cursor))).execute()
            },
            mapper = { queryData ->
                val pageInfo = queryData.users?.pageInfo!!
                PaginationPage(data = queryData.toPlayersList()!!, hasNextPage = pageInfo.hasNextPage, endCursor = pageInfo.endCursor)
            }
        )
    }

    override suspend fun getPlayerDetails(
        playerUid: String
    ) : Resource<PlayerDetails> {
        return executeApolloQuery(
            request = {
                apolloClient.query(
                    UsersQuery(filter =
                    Optional.present(
                        UserFilterInput(firebaseUserId = Optional.present(
                            StringOperationFilterInput(
                                eq = Optional.present(playerUid)
                            )
                        ))
                    ))
                ).execute()
            },
            mapper = { data ->
                data.toPlayerDetails()!!
            }
        )
    }

    override suspend fun getInfoAboutMe(): Resource<PlayerDetails> {
        return executeApolloQuery(
            request = {
                apolloClient.query(
                    MeQuery()
                ).execute()
            },
            mapper = { data ->
                data.me.detailsUserFragment.toPlayerDetails()
            }
        )
    }

    override suspend fun getIncomingMeetings(filters: MeetingsFilter, myUid: String): Resource<List<Meeting>> {
        return executeApolloQuery(
            request = {
                apolloClient.query(
                    IncomingOffersQuery(
                        offersFilter = filters.toOfferFilterInput()
                    )
                ).execute()
            },
            mapper = { data ->
                data.incomingOffers.let { beforeMappingData ->
                    beforeMappingData.map {
                        it.toMeeting(myUid)
                    }
                }
            }
        )
    }

    override suspend fun getNotifications(
        cursor: String?,
        pageSize: Int
    ): Resource<PaginationPage<UserNotification>> {
        return executeApolloQuery(
            request = {
                apolloClient.query(
                    NotificationsQuery(
                        first = Optional.present(pageSize),
                        cursor = Optional.presentIfNotNull(cursor)
                    )
                ).execute()
            },
            mapper = { data ->
                PaginationPage(
                    hasNextPage = data.notifications!!.pageInfo.hasNextPage,
                    endCursor = data.notifications.pageInfo.endCursor,
                    data = data.notifications.nodes!!.map {
                        UserNotification(
                            tittle = UiText.NonTranslatable(it.title),
                            text = UiText.NonTranslatable(it.body),
                            date = UiDate(OffsetDateTime.parse(it.dateTime.toString()))
                        )
                    }
                )
            }
        )
    }

    override suspend fun getOffersToAccept(
        filters: OffersFilter,
        cursor: String?,
        pageSize: Int
    ): Resource<PaginationPage<GameOfferToAccept>> {
        return executeApolloQuery(
            request = {
                apolloClient.query(
                    ResponsesQuery(
                        first = Optional.present(50),
                        otherFilters =
                            Optional.presentIfNotNull(
                                listOfNotNull(
                                    filters.sportFilter?.let {
                                        OfferFilterInput(sport = Optional.present(
                                            SportTypeOperationFilterInput(eq = Optional.present(it.toSportType()))
                                        ))
                                    },
                                    OfferFilterInput(
                                        dateTime = Optional.present(
                                            ComparableDateTimeOperationFilterInput(
                                                gte = Optional.present(OffsetDateTime.now().toString())
                                            )
                                        )
                                    ),
                                ).ifEmpty { null }
                            )
                    )
                ).execute()
            },
            mapper = { data ->
                val pageInfo = data.myOffers?.pageInfo!!
                PaginationPage(data = data.toGameOfferToAcceptList()!!, hasNextPage = pageInfo.hasNextPage, endCursor = pageInfo.endCursor)

            }
        )
    }


    override suspend fun createOffer(offerParams: AddGameOfferParams): Resource<String> {
        return withContext(ioDispatcher) {
            executeApolloMutation(
                request = {
                    apolloClient.mutation(CreateOfferMutation(offerParams.toCreateOfferInput())).execute()
                },
                returnValue = {
                    it.createOffer.offerId.toString()
                }
            )
        }
    }

    override suspend fun sendOfferToAccept(offerUid: String): Resource<String> {
        return withContext(ioDispatcher) {
            executeApolloMutation(
                request = {
                    apolloClient.mutation(
                        CreateResponseMutation(
                            CreateResponseInput(offerId = offerUid, description = "")
                        )
                    ).execute()
                },
                validateResult = {
                    it.createResponse?.responseId != null
                },
                returnValue = {
                    it.createResponse?.responseId.toString()
                }
            )
        }
    }

    override suspend fun acceptOfferToAccept(offerToAcceptUid: String): Resource<Unit> {
        return withContext(ioDispatcher) {
            executeApolloMutation(
                request = {
                    apolloClient.mutation(AcceptResponseMutation(responseId = offerToAcceptUid)).execute()
                }
            ).asUnitResource()
        }
    }

    override suspend fun updateUserInfo(userUpdateInfoParams: UserUpdateInfoParams): Resource<Unit> {
        return withContext(ioDispatcher) {
            executeApolloMutation(
                request = {
                    apolloClient.mutation(
                        UpdateUserMutation(
                            userUpdateInfoParams.toUpdateUserInput()
                        )
                    ).execute()
                }
            ).asUnitResource()
        }
    }

    override suspend fun updateAdvanceLevelInfo(level: Pair<Sport, AdvanceLevel>): Resource<Unit> {
        return withContext(ioDispatcher) {
            executeApolloMutation(
                request = {
                    apolloClient.mutation(SetSkillMutation(level.toSetSkillInput())).execute()
                }
            ).asUnitResource()
        }
    }

    override suspend fun deleteMyOffer(offerId: String): Resource<Unit> {
        return withContext(ioDispatcher) {
            executeApolloMutation(
                request = {
                    apolloClient.mutation(DeleteOfferMutation(offerId)).execute()
                }
            ).asUnitResource()
        }
    }

    override suspend fun deleteMyOfferToAccept(offerToAcceptUid: String): Resource<Unit> {
        return withContext(ioDispatcher) {
            executeApolloMutation(
                request = {
                    apolloClient.mutation(DeleteResponseMutation(offerToAcceptUid)).execute()
                }
            ).asUnitResource()
        }
    }

    override suspend fun sendNotificationToken(token: String, deviceId: String): Resource<Unit> {
        return withContext(ioDispatcher) {
            executeApolloMutation(
                request = {
                    apolloClient.mutation(UpdateNotificationTokenMutation(SetDeviceInput(deviceId = deviceId, token = token))).execute()
                }
            ).asUnitResource()
        }

    }

    override suspend fun rejectOfferToAccept(offerToAcceptUid: String): Resource<Unit> {
        return withContext(ioDispatcher) {
            executeApolloMutation(
                request = {
                    apolloClient.mutation(RejectResponseMutation(responseId = offerToAcceptUid)).execute()
                }
            ).asUnitResource()
        }
    }


    private suspend fun <T : Operation.Data> executeApolloMutation(
        request: suspend () -> ApolloResponse<T>,
        validateResult: (T) -> Boolean = { true },
        returnValue: (T) -> String = { "" },
        onDataSuccessfullyReceived: (T) -> Unit = {}
    ): Resource<String> {
        return withContext(ioDispatcher) {
            val response = try {
                request()
            } catch (e: Exception) {
                ensureActive()
                e.printStackTrace()
                null
            }
            if (!response?.errors.isNullOrEmpty()) {
                val message = response?.errors?.map {
                    it.message
                }?.reduce { acc, s -> "$acc$s " }
                return@withContext Resource.Error(UiText.NonTranslatable("Error: $message"))
            }
            val responseData = response?.dataAssertNoErrors
            if (responseData != null && !validateResult(responseData)) {
                return@withContext Resource.Error(defaultRequestError)
            }
            return@withContext response?.data?.let {
                onDataSuccessfullyReceived(it)
                Resource.Success(returnValue(it))
            } ?: let {
                Resource.Error(
                    message = UiText.NonTranslatable(
                        response?.errors?.firstOrNull()?.message ?: "Request error"
                    )
                )
            }
        }
    }

    private suspend fun <T : Operation.Data, D> executeApolloQuery(
        request: suspend () -> ApolloResponse<T>,
        mapper: (T) -> D
    ): Resource<D> {
        return withContext(ioDispatcher) {
            val response = try {
                request().dataAssertNoErrors
            } catch (e: Exception) {
                ensureActive()
                e.printStackTrace()
                null
            }

            response?.let {
                Resource.Success(mapper(it))
            } ?: Resource.Error(defaultRequestError)
        }
    }
}