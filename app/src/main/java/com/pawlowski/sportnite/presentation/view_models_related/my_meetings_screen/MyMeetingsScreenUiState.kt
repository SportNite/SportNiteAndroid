package com.pawlowski.sportnite.presentation.view_models_related.my_meetings_screen

import com.pawlowski.sportnite.presentation.models.GameOffer
import com.pawlowski.sportnite.presentation.models.GameOfferToAccept
import com.pawlowski.sportnite.presentation.models.Meeting
import com.pawlowski.utils.UiData

data class MyMeetingsScreenUiState(
    val offersToAccept: UiData<List<GameOfferToAccept>>,
    val incomingMeetings: UiData<List<Meeting>>,
    val myOffers: UiData<List<GameOffer>>,
    val historicalMeetings: UiData<List<Meeting>>
)
