package com.pawlowski.sportnite.data.remote

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.pawlowski.sportnite.*
import com.pawlowski.sportnite.data.mappers.toCreateOfferInput
import com.pawlowski.sportnite.data.mappers.toUpdateUserInput
import com.pawlowski.sportnite.domain.models.AddGameOfferParams
import com.pawlowski.sportnite.domain.models.UserUpdateInfoParams
import com.pawlowski.sportnite.type.CreateResponseInput
import com.pawlowski.sportnite.type.SetSkillInput
import com.pawlowski.sportnite.utils.Resource
import com.pawlowski.sportnite.utils.UiText
import com.pawlowski.sportnite.utils.asUnitResource
import com.pawlowski.sportnite.utils.defaultRequestError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GraphQLService @Inject constructor(
    private val apolloClient: ApolloClient,
    private val ioDispatcher: CoroutineDispatcher,
) : IGraphQLService {
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

    override suspend fun updateAdvanceLevelInfo(setSkillInput: SetSkillInput): Resource<Unit> {
        return withContext(ioDispatcher) {
            executeApolloMutation(
                request = {
                    apolloClient.mutation(SetSkillMutation(setSkillInput)).execute()
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
            val responseData = response?.data
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