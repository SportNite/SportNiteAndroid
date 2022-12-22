package com.pawlowski.sportnite.data.mappers

import com.apollographql.apollo3.api.Optional
import com.pawlowski.sportnite.OffersQuery
import com.pawlowski.sportnite.ResponsesQuery
import com.pawlowski.sportnite.domain.models.AddGameOfferParams
import com.pawlowski.sportnite.presentation.models.GameOffer
import com.pawlowski.sportnite.presentation.models.GameOfferToAccept
import com.pawlowski.sportnite.presentation.models.Player
import com.pawlowski.sportnite.presentation.models.Sport
import com.pawlowski.sportnite.presentation.ui.utils.getGameOfferForPreview
import com.pawlowski.sportnite.presentation.ui.utils.getPlayerForPreview
import com.pawlowski.sportnite.presentation.ui.utils.getSportForPreview
import com.pawlowski.sportnite.type.CreateOfferInput
import com.pawlowski.sportnite.type.SportType
import com.pawlowski.sportnite.utils.UiDate
import java.time.OffsetDateTime

fun AddGameOfferParams.toCreateOfferInput(): CreateOfferInput {
    return CreateOfferInput(
        description = Optional.present(this.additionalNotes),
        dateTime = this.date.offsetDateTimeDate.toString(),
        sport = this.sport.toSportType(),
        placeId = 0,
        street = Optional.present(this.placeOrAddress),
        city = Optional.present(this.city)
    )
}

fun OffersQuery.Node.toGameOffer(): GameOffer {
    return GameOffer(
        city = this.city?:"",
        placeOrAddress = this.street?:"",
        additionalNotes = this.description,
        offerUid = this.offerId.toString(),
        sport = this.sport.toSport(),
        date = UiDate(OffsetDateTime.parse(this.dateTime.toString())),
        owner = this.user!!.toPlayer()
    )
}

fun OffersQuery.User.toPlayer(): Player {
    return getPlayerForPreview().copy(
        uid = this.userId.toString(),
        name= this.name,
    )
}

fun ResponsesQuery.User.toPlayer(): Player {
    return getPlayerForPreview().copy(
        uid = this.userId.toString(),
        name= this.name,
    )
}

fun OffersQuery.Data.toGameOfferList(): List<GameOffer>? {
    return this.offers?.nodes?.mapNotNull {
        it?.toGameOffer()
    }
}

fun ResponsesQuery.Response.toGameOfferToAccept(): GameOfferToAccept {
    return GameOfferToAccept(
        offerToAcceptUid = this.offerId.toString(),
        from = this.user.toPlayer(),
        offer = getGameOfferForPreview().copy(offerUid = this.offerId.toString()) //TODO fix when backand error will be fixed
    )
}

fun ResponsesQuery.Data.toGameOfferToAcceptList(): List<GameOfferToAccept>? {
    return this.myOffers?.nodes?.flatMap { node ->
        node.responses.map {
            it.toGameOfferToAccept()
        }
    }
}

fun Sport.toSportType(): SportType {
    return SportType.TENNIS
}

fun SportType.toSport(): Sport {
    return getSportForPreview()
}