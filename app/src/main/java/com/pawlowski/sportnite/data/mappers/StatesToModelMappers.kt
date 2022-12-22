package com.pawlowski.sportnite.data.mappers

import com.pawlowski.sportnite.presentation.mappers.asGameOffer
import com.pawlowski.sportnite.presentation.models.GameOffer
import com.pawlowski.sportnite.presentation.models.GameOfferToAccept

fun GameOfferToAccept.toGameOffer(): GameOffer {
    return this.asGameOffer()
}