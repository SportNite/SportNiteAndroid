package com.pawlowski.sports.view_model_related

import com.pawlowski.models.GameOffer
import org.orbitmvi.orbit.ContainerHost

interface ISportScreenViewModel: ContainerHost<SportScreenUiState, SportScreenSideEffect> {
    fun sendGameOfferToAccept(gameOffer: GameOffer)
    fun acceptOfferToAccept(gameOfferToAcceptId: String)
    fun refresh()
    fun deleteMyOfferToAccept(offerToAcceptId: String)
    fun rejectOfferToAccept(offerToAcceptUid: String)
}