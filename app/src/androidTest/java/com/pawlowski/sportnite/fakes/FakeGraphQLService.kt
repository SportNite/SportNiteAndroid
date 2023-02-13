package com.pawlowski.sportnite.fakes

import com.pawlowski.models.*
import com.pawlowski.models.params_models.*
import com.pawlowski.network.data.IGraphQLService
import com.pawlowski.utils.PaginationPage
import com.pawlowski.utils.Resource

object FakeGraphQLService: IGraphQLService {

    private fun <T>singlePage(data: List<T>): PaginationPage<T> {
        return PaginationPage(
            hasNextPage = false,
            endCursor = "endCursor",
            data = data
        )
    }

    private fun <T>successSinglePageResource(data: List<T>): Resource<PaginationPage<T>> {
        return Resource.Success(singlePage(data = data))
    }

    override suspend fun getOffers(
        filters: OffersFilter,
        cursor: String?,
        pageSize: Int
    ): Resource<PaginationPage<GameOffer>> {
        return successSinglePageResource(listOf())
    }

    override suspend fun getPlayers(
        filters: PlayersFilter,
        cursor: String?,
        pageSize: Int
    ): Resource<PaginationPage<Player>> {
        return successSinglePageResource(listOf())
    }

    override suspend fun getOffersToAccept(
        filters: OffersFilter,
        cursor: String?,
        pageSize: Int
    ): Resource<PaginationPage<GameOfferToAccept>> {
        return successSinglePageResource(listOf())
    }

    override suspend fun getPlayerDetails(playerUid: String): Resource<PlayerDetails> {
        TODO("Not yet implemented")
    }

    override suspend fun getInfoAboutMe(): Resource<PlayerDetails> {
        TODO("Not yet implemented")
    }

    override suspend fun getIncomingMeetings(
        filters: MeetingsFilter,
        myUid: String
    ): Resource<List<Meeting>> {
        TODO("Not yet implemented")
    }

    override suspend fun getNotifications(
        cursor: String?,
        pageSize: Int
    ): Resource<PaginationPage<UserNotification>> {
        return successSinglePageResource(listOf())
    }

    override suspend fun createOffer(offerParams: AddGameOfferParams): Resource<String> {
        TODO("Not yet implemented")
    }

    override suspend fun sendOfferToAccept(offerUid: String): Resource<String> {
        TODO("Not yet implemented")
    }

    override suspend fun acceptOfferToAccept(offerToAcceptUid: String): Resource<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserInfo(userUpdateInfoParams: UserUpdateInfoParams): Resource<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateAdvanceLevelInfo(level: Pair<Sport, AdvanceLevel>): Resource<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMyOffer(offerId: String): Resource<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMyOfferToAccept(offerToAcceptUid: String): Resource<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun sendNotificationToken(token: String, deviceId: String): Resource<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun rejectOfferToAccept(offerToAcceptUid: String): Resource<Unit> {
        TODO("Not yet implemented")
    }
}