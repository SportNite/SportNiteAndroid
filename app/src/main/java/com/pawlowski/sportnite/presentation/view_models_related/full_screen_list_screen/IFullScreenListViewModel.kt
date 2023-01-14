package com.pawlowski.sportnite.presentation.view_models_related.full_screen_list_screen

import androidx.paging.PagingData
import com.pawlowski.sportnite.presentation.models.GameOffer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.orbitmvi.orbit.ContainerHost

interface IFullScreenListViewModel: ContainerHost<FullScreenListUiState, FullScreenListSideEffect> {
    fun deleteMyOfferToAccept(offer: GameOffer)
    fun sendOfferToAccept(offer: GameOffer)

    val offersFlow: Flow<PagingData<GameOffer>>
    val dataTypeFlow: StateFlow<FullScreenDataType>
}