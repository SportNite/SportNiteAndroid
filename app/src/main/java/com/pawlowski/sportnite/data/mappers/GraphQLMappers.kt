package com.pawlowski.sportnite.data.mappers

import com.apollographql.apollo3.api.Optional
import com.pawlowski.sportnite.domain.models.AddGameOfferParams
import com.pawlowski.sportnite.presentation.models.Sport
import com.pawlowski.sportnite.type.CreateOfferInput
import com.pawlowski.sportnite.type.SportType

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

fun Sport.toSportType(): SportType {
    return SportType.TENNIS
}