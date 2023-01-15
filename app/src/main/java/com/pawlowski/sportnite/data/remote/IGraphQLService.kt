package com.pawlowski.sportnite.data.remote

import com.pawlowski.sportnite.domain.models.AddGameOfferParams
import com.pawlowski.sportnite.domain.models.UserUpdateInfoParams
import com.pawlowski.sportnite.type.SetSkillInput
import com.pawlowski.sportnite.utils.Resource

interface IGraphQLService {
    suspend fun createOffer(offerParams: AddGameOfferParams): Resource<String>
    suspend fun sendOfferToAccept(offerUid: String): Resource<String>
    suspend fun acceptOfferToAccept(offerToAcceptUid: String): Resource<Unit>
    suspend fun updateUserInfo(userUpdateInfoParams: UserUpdateInfoParams): Resource<Unit>
    suspend fun updateAdvanceLevelInfo(setSkillInput: SetSkillInput): Resource<Unit>
    suspend fun deleteMyOffer(offerId: String): Resource<Unit>
    suspend fun deleteMyOfferToAccept(offerToAcceptUid: String): Resource<Unit>
}