package com.pawlowski.sportnite.data.mappers

import com.pawlowski.sportnite.presentation.mappers.asGameOffer
import com.pawlowski.models.GameOffer
import com.pawlowski.models.GameOfferToAccept

fun GameOfferToAccept.toGameOffer(): GameOffer {
    return this.asGameOffer()
}