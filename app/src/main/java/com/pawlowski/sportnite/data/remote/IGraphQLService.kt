package com.pawlowski.sportnite.data.remote

import com.pawlowski.sportnite.domain.models.*
import com.pawlowski.sportnite.presentation.models.*
import com.pawlowski.sportnite.type.SetSkillInput
import com.pawlowski.sportnite.utils.PaginationPage
import com.pawlowski.sportnite.utils.Resource

interface IGraphQLService {
    //Queries

    suspend fun getOffers(filters: OffersFilter, cursor: String? = null, pageSize: Int = 10): Resource<PaginationPage<GameOffer>>
    suspend fun getPlayers(filters: PlayersFilter, cursor: String? = null, pageSize: Int = 10): Resource<PaginationPage<Player>>
    suspend fun getOffersToAccept(filters: OffersFilter, cursor: String? = null, pageSize: Int = 10): Resource<PaginationPage<GameOfferToAccept>>
    suspend fun getPlayerDetails(playerUid: String): Resource<PlayerDetails>
    suspend fun getInfoAboutMe(): Resource<PlayerDetails>
    suspend fun getIncomingMeetings(filters: MeetingsFilter, myUid: String): Resource<List<Meeting>>

    //Mutations
    suspend fun createOffer(offerParams: AddGameOfferParams): Resource<String>
    suspend fun sendOfferToAccept(offerUid: String): Resource<String>
    suspend fun acceptOfferToAccept(offerToAcceptUid: String): Resource<Unit>
    suspend fun updateUserInfo(userUpdateInfoParams: UserUpdateInfoParams): Resource<Unit>
    suspend fun updateAdvanceLevelInfo(setSkillInput: SetSkillInput): Resource<Unit>
    suspend fun deleteMyOffer(offerId: String): Resource<Unit>
    suspend fun deleteMyOfferToAccept(offerToAcceptUid: String): Resource<Unit>
}