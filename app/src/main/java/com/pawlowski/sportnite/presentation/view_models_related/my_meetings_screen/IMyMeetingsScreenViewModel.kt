package com.pawlowski.sportnite.presentation.view_models_related.my_meetings_screen

import com.pawlowski.models.GameOffer
import org.orbitmvi.orbit.ContainerHost

interface IMyMeetingsScreenViewModel: ContainerHost<MyMeetingsScreenUiState, MyMeetingsScreenSideEffect> {
    fun deleteOffer(offer: GameOffer)
    fun refresh()
    fun acceptOfferToAccept(offerToAcceptUid: String)
}