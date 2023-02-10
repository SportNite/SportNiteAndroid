package com.pawlowski.mymeetings.view_model_related

import com.pawlowski.models.GameOffer
import org.orbitmvi.orbit.ContainerHost

interface IMyMeetingsScreenViewModel: ContainerHost<MyMeetingsScreenUiState, MyMeetingsScreenSideEffect> {
    fun deleteOffer(offer: GameOffer)
    fun refresh()
    fun acceptOfferToAccept(offerToAcceptUid: String)
    fun rejectOfferToAccept(offerToAcceptUid: String)
}