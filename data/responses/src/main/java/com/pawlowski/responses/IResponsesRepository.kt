package com.pawlowski.responses

import com.pawlowski.models.GameOfferToAccept
import com.pawlowski.models.Sport
import com.pawlowski.utils.Resource
import com.pawlowski.utils.UiData
import kotlinx.coroutines.flow.Flow

interface IResponsesRepository {
    fun getOffersToAccept(sportFilter: Sport?): Flow<UiData<List<GameOfferToAccept>>>

    suspend fun sendOfferToAccept(offerUid: String): Resource<String>
    suspend fun acceptOfferToAccept(offerToAcceptUid: String): Resource<Unit>
    suspend fun deleteMyOfferToAccept(offerToAcceptUid: String): Resource<Unit>
    suspend fun rejectOfferToAccept(offerToAcceptUid: String): Resource<Unit>
}