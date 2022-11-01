package com.pawlowski.sportnite.presentation.models

data class GameOfferToAccept(
    val offer: GameOffer,
    val offerToAcceptUid: String,
    val from: Player
)
