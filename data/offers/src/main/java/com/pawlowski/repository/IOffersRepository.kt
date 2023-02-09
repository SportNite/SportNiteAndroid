package com.pawlowski.repository

import androidx.paging.PagingData
import com.pawlowski.models.GameOffer
import com.pawlowski.models.Sport
import com.pawlowski.models.params_models.AddGameOfferParams
import com.pawlowski.models.params_models.OffersFilter
import com.pawlowski.utils.Resource
import com.pawlowski.utils.UiData
import kotlinx.coroutines.flow.Flow

interface IOffersRepository {
    fun getGameOffers(sportFilter: Sport? = null): Flow<UiData<List<GameOffer>>>
    fun getMyGameOffers(sportFilter: Sport? = null): Flow<UiData<List<GameOffer>>>
    fun getPagedOffers(filter: OffersFilter): Flow<PagingData<GameOffer>>
    suspend fun addGameOffer(gameParams: AddGameOfferParams): Resource<Unit>
    suspend fun deleteMyOffer(offerId: String): Resource<Unit>

}