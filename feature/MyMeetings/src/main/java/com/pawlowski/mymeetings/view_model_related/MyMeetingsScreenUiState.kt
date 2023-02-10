package com.pawlowski.mymeetings.view_model_related

import com.pawlowski.models.GameOffer
import com.pawlowski.models.GameOfferToAccept
import com.pawlowski.models.Meeting
import com.pawlowski.utils.UiData

data class MyMeetingsScreenUiState(
    val offersToAccept: UiData<List<GameOfferToAccept>>,
    val incomingMeetings: UiData<List<Meeting>>,
    val myOffers: UiData<List<GameOffer>>,
    val historicalMeetings: UiData<List<Meeting>>
)
