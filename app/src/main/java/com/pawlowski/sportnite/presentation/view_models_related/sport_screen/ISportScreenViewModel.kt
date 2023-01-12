package com.pawlowski.sportnite.presentation.view_models_related.sport_screen

import com.pawlowski.sportnite.presentation.models.GameOffer
import com.pawlowski.sportnite.presentation.models.GameOfferToAccept
import org.orbitmvi.orbit.ContainerHost

interface ISportScreenViewModel: ContainerHost<SportScreenUiState, SportScreenSideEffect> {
    fun sendGameOfferToAccept(gameOffer: GameOffer)
    fun acceptOfferToAccept(gameOfferToAcceptId: String)
    fun refresh()
}