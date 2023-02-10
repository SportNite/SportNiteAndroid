package com.pawlowski.fullscreenlist.view_model_related

import androidx.paging.PagingData
import com.pawlowski.models.GameOffer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.orbitmvi.orbit.ContainerHost

interface IFullScreenListViewModel: ContainerHost<FullScreenListUiState, FullScreenListSideEffect> {
    fun deleteMyOfferToAccept(offer: GameOffer)
    fun sendOfferToAccept(offer: GameOffer)
    fun rejectOfferToAccept(offerToAcceptUid: String)

    val offersFlow: Flow<PagingData<GameOffer>>
    val dataTypeFlow: StateFlow<FullScreenDataType>
}