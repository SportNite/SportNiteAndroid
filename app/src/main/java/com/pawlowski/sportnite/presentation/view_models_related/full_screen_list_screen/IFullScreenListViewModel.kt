package com.pawlowski.sportnite.presentation.view_models_related.full_screen_list_screen

import androidx.paging.PagingData
import com.pawlowski.sportnite.presentation.models.GameOffer
import com.pawlowski.sportnite.presentation.models.Meeting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.orbitmvi.orbit.ContainerHost

interface IFullScreenListViewModel: ContainerHost<FullScreenListUiState, FullScreenListSideEffect> {
    fun deleteMyOfferToAccept(offerToAcceptUid: String)
    fun sendOfferToAccept(offerUid: String)

    val offersFlow: Flow<PagingData<GameOffer>>
    val meetingsFlow: Flow<PagingData<Meeting>>
    val dataTypeFlow: StateFlow<FullScreenDataType>
}