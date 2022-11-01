package com.pawlowski.sportnite.presentation.view_models_related.sport_screen

import com.pawlowski.sportnite.presentation.models.GameOffer
import com.pawlowski.sportnite.presentation.models.GameOfferToAccept
import com.pawlowski.sportnite.presentation.models.Meeting
import com.pawlowski.sportnite.presentation.models.Sport
import com.pawlowski.sportnite.utils.UiData

data class SportScreenUiState(
    val sport: Sport?,
    val myMeetings: UiData<List<Meeting>> = UiData.Loading(),
    val otherPlayers: UiData<List<GameOffer>> = UiData.Loading(),
    val gameOffers: UiData<List<GameOffer>> = UiData.Loading(),
    val offersToAccept: UiData<List<GameOfferToAccept>> = UiData.Loading(),
)
