package com.pawlowski.sportnite.data.mappers

import com.apollographql.apollo3.api.Optional
import com.pawlowski.sportnite.*
import com.pawlowski.sportnite.domain.models.AddGameOfferParams
import com.pawlowski.sportnite.domain.models.UserUpdateInfoParams
import com.pawlowski.sportnite.presentation.models.*
import com.pawlowski.sportnite.presentation.ui.utils.getGameOfferForPreview
import com.pawlowski.sportnite.presentation.ui.utils.getPlayerForPreview
import com.pawlowski.sportnite.presentation.ui.utils.getSportForPreview
import com.pawlowski.sportnite.type.CreateOfferInput
import com.pawlowski.sportnite.type.ResponseStatus
import com.pawlowski.sportnite.type.SportType
import com.pawlowski.sportnite.type.UpdateUserInput
import com.pawlowski.sportnite.utils.UiDate
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.Period

fun AddGameOfferParams.toCreateOfferInput(): CreateOfferInput {
    return CreateOfferInput(
        description = Optional.present(this.additionalNotes),
        dateTime = this.date.offsetDateTimeDate.toString(),
        sport = this.sport.toSportType(),
        placeId = 0,
        street = this.placeOrAddress,
        city = this.city
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
        uid = this.firebaseUserId,
        name= this.name,
    )
}

fun MyOffersQuery.User.toPlayer(): Player {
    return getPlayerForPreview().copy(
        uid = this.firebaseUserId,
        name= this.name,
    )
}

fun MyOffersQuery.Data.toGameOfferList() : List<GameOffer>? {
    return this.myOffers?.nodes?.map {
        it.toGameOffer()
    }
}

fun MyOffersQuery.Node.toGameOffer(): GameOffer {
    return GameOffer(
        city = this.city,
        placeOrAddress = this.street,
        additionalNotes = this.description,
        offerUid = this.offerId.toString(),
        sport = this.sport.toSport(),
        date = UiDate(OffsetDateTime.parse(this.dateTime.toString())),
        owner = this.user.toPlayer()
    )
}

fun ResponsesQuery.User.toPlayer(): Player {
    return getPlayerForPreview().copy(
        uid = this.firebaseUserId,
        name= this.name,
    )
}

fun IncomingOffersQuery.User.toPlayer(): Player {
    return getPlayerForPreview().copy(
        uid = this.firebaseUserId,
        name= this.name,
    )
}

fun IncomingOffersQuery.User1.toPlayer(): Player {
    return getPlayerForPreview().copy(
        uid = this.firebaseUserId,
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

fun UserUpdateInfoParams.toUpdateUserInput(): UpdateUserInput {
    return UpdateUserInput(
        name = Optional.present(name),
        availability = Optional.present(availability),
        birthDate = Optional.present(birthDate.offsetDateTimeDate.toString()),
        avatar = Optional.presentIfNotNull(photoUrl)
    )
}

fun IncomingOffersQuery.IncomingOffer.toMeeting(myUid: String): Meeting {
    val opponent = if(user!!.toString() != myUid)
        user.toPlayer()
    else
        responses
            .first {
                it.status == ResponseStatus.APPROVED
            }
            .user.toPlayer()

    return Meeting(
        opponent = opponent,
        sport = sport.toSport(),
        placeOrAddress = street?:"",
        city = city?:"",
        date = UiDate(OffsetDateTime.parse(dateTime.toString())),
        additionalNotes = description,
        meetingUid = offerId.toString()
    )
}

fun UsersQuery.Data.toPlayersList(): List<Player>? {
    return this.users?.nodes?.mapNotNull {
        it?.toPlayer()
    }
}

fun UsersQuery.Node.toPlayer(): Player {
    val ageInYears = Period.between(OffsetDateTime.parse(birthDate.toString()).toLocalDate(),LocalDate.now()).years
    return Player(
        uid = firebaseUserId,
        name = name,
        photoUrl = avatar,
        advanceLevel = AdvanceLevel.NRTP(6.0), //TODO: Change
        age = ageInYears,
        phoneNumber = "" //TODO
    )
}

fun Sport.toSportType(): SportType {
    return SportType.TENNIS
}

fun SportType.toSport(): Sport {
    return getSportForPreview()
}