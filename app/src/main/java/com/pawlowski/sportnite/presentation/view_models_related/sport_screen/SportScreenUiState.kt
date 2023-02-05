package com.pawlowski.sportnite.presentation.view_models_related.sport_screen

import com.pawlowski.models.*
import com.pawlowski.utils.UiData

data class SportScreenUiState(
    val sport: Sport,
    val myMeetings: UiData<List<Meeting>> = UiData.Loading(),
    val otherPlayers: UiData<List<Player>> = UiData.Loading(),
    val gameOffers: UiData<List<GameOffer>> = UiData.Loading(),
    val offersToAccept: UiData<List<GameOfferToAccept>> = UiData.Loading(),
)
