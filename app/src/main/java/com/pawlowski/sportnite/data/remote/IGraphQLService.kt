package com.pawlowski.sportnite.data.remote

import com.pawlowski.sportnite.domain.models.*
import com.pawlowski.sportnite.presentation.models.GameOffer
import com.pawlowski.sportnite.presentation.models.Player
import com.pawlowski.sportnite.type.SetSkillInput
import com.pawlowski.sportnite.utils.PaginationPage
import com.pawlowski.sportnite.utils.Resource

interface IGraphQLService {
    //Queries

    suspend fun getOffers(filters: OffersFilter, cursor: String? = null, pageSize: Int = 10): Resource<PaginationPage<GameOffer>>
    suspend fun getPlayers(filters: PlayersFilter, cursor: String? = null, pageSize: Int = 10): Resource<PaginationPage<Player>>
    //Mutations

    suspend fun createOffer(offerParams: AddGameOfferParams): Resource<String>
    suspend fun sendOfferToAccept(offerUid: String): Resource<String>
    suspend fun acceptOfferToAccept(offerToAcceptUid: String): Resource<Unit>
    suspend fun updateUserInfo(userUpdateInfoParams: UserUpdateInfoParams): Resource<Unit>
    suspend fun updateAdvanceLevelInfo(setSkillInput: SetSkillInput): Resource<Unit>
    suspend fun deleteMyOffer(offerId: String): Resource<Unit>
    suspend fun deleteMyOfferToAccept(offerToAcceptUid: String): Resource<Unit>
}