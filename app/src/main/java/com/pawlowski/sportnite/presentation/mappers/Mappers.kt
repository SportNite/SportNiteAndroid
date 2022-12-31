package com.pawlowski.sportnite.presentation.mappers

import com.pawlowski.sportnite.domain.models.AddGameOfferParams
import com.pawlowski.sportnite.presentation.models.GameOffer
import com.pawlowski.sportnite.presentation.models.GameOfferToAccept
import com.pawlowski.sportnite.presentation.ui.utils.getPlayerForPreview

fun GameOfferToAccept.asGameOffer(): GameOffer {
    return this.offer.copy(owner = this.from, offerUid = this.offerToAcceptUid)
}

fun AddGameOfferParams.toGameOffer(offerId: String, playerName: String): GameOffer {
    return GameOffer(
        owner = getPlayerForPreview().copy(name = playerName),
        date = date,
        placeOrAddress = placeOrAddress,
        city = city,
        additionalNotes = additionalNotes,
        sport = sport,
        offerUid = offerId
    )
}