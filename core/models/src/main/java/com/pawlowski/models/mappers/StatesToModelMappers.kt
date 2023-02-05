package com.pawlowski.models.mappers

import com.pawlowski.models.mappers.asGameOffer
import com.pawlowski.models.GameOffer
import com.pawlowski.models.GameOfferToAccept

fun GameOfferToAccept.toGameOffer(): GameOffer {
    return this.asGameOffer()
}