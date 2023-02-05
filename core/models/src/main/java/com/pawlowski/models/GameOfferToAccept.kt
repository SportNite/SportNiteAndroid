package com.pawlowski.models

data class GameOfferToAccept(
    val offer: GameOffer,
    val offerToAcceptUid: String,
    val from: Player
)
