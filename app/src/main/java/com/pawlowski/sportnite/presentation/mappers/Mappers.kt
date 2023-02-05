package com.pawlowski.sportnite.presentation.mappers

import com.pawlowski.models.*
import com.pawlowski.models.params_models.AddGameOfferParams
import com.pawlowski.sportnite.presentation.ui.utils.getPlayerForPreview

fun GameOfferToAccept.asGameOffer(): GameOffer {
    return this.offer.copy(owner = this.from, offerUid = this.offerToAcceptUid)
}

fun PlayerDetails.toPlayer(): Player {
    return Player(
        uid = playerUid,
        name = playerName,
        phoneNumber = this.contact[0],
        age = age,
        photoUrl = playerPhotoUrl
    )
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

fun AdvanceLevel.parse(): String {
    return when(this) {
        is AdvanceLevel.NRTP -> {
            "NRTP:$nrtpLevel"
        }
        is AdvanceLevel.DefaultLevel -> {
            "Level:$level"
        }
    }
}

fun getAdvanceLevelFromParsedString(string: String): AdvanceLevel {
    return string.split(":").let {
        when(it[0]) {
            "NRTP" -> {
                AdvanceLevel.NRTP(it[1].toDouble())
            }
            "Level" -> {
                AdvanceLevel.DefaultLevel(it[1].toInt())
            }
            else -> throw Exception("Unknown parse type")
        }
    }
}