package com.pawlowski.sportnite.presentation.mappers

import com.pawlowski.sportnite.presentation.models.GameOffer
import com.pawlowski.sportnite.presentation.models.GameOfferToAccept

fun GameOfferToAccept.asGameOffer(): GameOffer {
    return this.offer.copy(owner = this.from)
}