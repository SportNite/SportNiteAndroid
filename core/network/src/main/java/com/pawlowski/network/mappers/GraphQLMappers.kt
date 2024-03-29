package com.pawlowski.network.mappers

import com.apollographql.apollo3.api.Optional
import com.pawlowski.models.*
import com.pawlowski.models.mappers.getSportFromSportId
import com.pawlowski.models.params_models.*
import com.pawlowski.network.fragment.DetailsUserFragment
import com.pawlowski.network.fragment.MediumUserFragment
import com.pawlowski.network.fragment.OfferFragment
import com.pawlowski.network.type.*
import com.pawlowski.models.mappers.toPlayer
import com.pawlowski.network.*

import com.pawlowski.utils.UiDate
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.Period


internal fun PlayersFilter.toUserFilterInput(): Optional<UserFilterInput> {
    return if (nameSearch == null && sportFilter == null && level == null)
        Optional.absent()
    else {
        val nameFilter = nameSearch?.let {
            UserFilterInput(
                name = Optional.present(
                    StringOperationFilterInput(
                        contains = Optional.present(
                            nameSearch
                        )
                    )
                ),
            )
        }
        val sportFilter = sportFilter?.let { filter ->
            UserFilterInput(
                skills = Optional.present(
                    value = ListFilterInputTypeOfSkillFilterInput(
                        some = Optional.present(
                            SkillFilterInput(
                                sport = Optional.present(
                                    SportTypeOperationFilterInput(
                                        eq = Optional.present(filter.toSportType())
                                    )
                                ),
                                and = Optional.presentIfNotNull(level?.let {
                                    null //TODO: Add level filters
                                })
                            )
                        )
                    )
                )
            )
        }
        Optional.present(
            UserFilterInput(and = Optional.presentIfNotNull(listOfNotNull(nameFilter, sportFilter)))
        )
    }
}

internal fun AddGameOfferParams.toCreateOfferInput(): CreateOfferInput {
    return CreateOfferInput(
        description = Optional.present(this.additionalNotes),
        dateTime = this.date.offsetDateTimeDate.toString(),
        sport = this.sport.toSportType(),
        placeId = 0,
        street = this.placeOrAddress,
        city = this.city
    )
}

internal fun OfferFragment.toGameOffer(userFragment: MediumUserFragment, myResponseIdIfExists: String? = null): GameOffer {
    return this.let {
        GameOffer(
            city = it.city,
            placeOrAddress = it.street,
            additionalNotes = it.description,
            offerUid = it.offerId.toString(),
            sport = it.sport.toSport(),
            date = UiDate(OffsetDateTime.parse(it.dateTime.toString())),
            owner = userFragment.toPlayer(),
            myResponseIdIfExists = myResponseIdIfExists
        )
    }
}

internal fun MediumUserFragment.toPlayer(): Player {
    val ageInYears = Period.between(
        OffsetDateTime.parse(this.birthDate.toString()).toLocalDate(),
        LocalDate.now()
    ).years
    return Player(
        uid = this.firebaseUserId,
        name = this.name,
        photoUrl = this.avatar,
        phoneNumber = this.phone?:"",
        age = ageInYears
    )
}

internal fun OffersQuery.Node.toGameOffer(): GameOffer {
    return this.offerFragment.toGameOffer(this.user.mediumUserFragment, this.myResponse?.responseId?.toString())
}


internal fun MyOffersQuery.Data.toGameOfferList(): List<GameOffer>? {
    return this.myOffers?.nodes?.map {
        it.toGameOffer()
    }
}

internal fun MyOffersQuery.Node.toGameOffer(): GameOffer {
    return this.offerFragment.toGameOffer(userFragment = this.user.mediumUserFragment, myResponseIdIfExists = null)
}

internal fun ResponsesQuery.User.toPlayer(): Player {
    return this.mediumUserFragment.toPlayer()
}

internal fun OffersQuery.Data.toGameOfferList(): List<GameOffer>? {
    return this.offers?.nodes?.mapNotNull {
        it?.toGameOffer()
    }
}

internal fun ResponsesQuery.Response.toGameOfferToAccept(): GameOfferToAccept {
    return GameOfferToAccept(
        offerToAcceptUid = this.responseId.toString(),
        from = this.user.toPlayer(),
        offer = GameOffer(
            city = this.offer.city,
            placeOrAddress = this.offer.street,
            additionalNotes = this.offer.description,
            offerUid = this.offerId.toString(),
            sport = this.offer.sport.toSport(),
            date = UiDate(OffsetDateTime.parse(this.offer.dateTime.toString())),
            owner = this.user.toPlayer() //TODO: Change if it's used
        )
    )
}

internal fun ResponsesQuery.Data.toGameOfferToAcceptList(): List<GameOfferToAccept>? {
    return this.myOffers?.nodes?.flatMap { node ->
        node.responses.filter {
            it.status != ResponseStatus.APPROVED && it.status != ResponseStatus.REJECTED
        }.map {
            it.toGameOfferToAccept()
        }
    }
}

internal fun UserUpdateInfoParams.toUpdateUserInput(): UpdateUserInput {
    return UpdateUserInput(
        name = Optional.present(name),
        availability = Optional.present(availability),
        birthDate = Optional.present(birthDate.offsetDateTimeDate.toString()),
        avatar = Optional.presentIfNotNull(photoUrl)
    )
}

internal fun IncomingOffersQuery.IncomingOffer.toMeeting(myUid: String): Meeting {
    val opponent = if (user.mediumUserFragment.firebaseUserId != myUid)
        user.mediumUserFragment.toPlayer()
    else
    {
        responses
            .first {
                it.status == ResponseStatus.APPROVED
            }
            .user.mediumUserFragment.toPlayer()
    }

    return Meeting(
        opponent = opponent,
        sport = sport.toSport(),
        placeOrAddress = street,
        city = city,
        date = UiDate(OffsetDateTime.parse(dateTime.toString())),
        additionalNotes = description,
        meetingUid = offerId.toString()
    )
}

internal fun UsersQuery.Data.toPlayersList(): List<Player>? {
    return this.users?.nodes?.mapNotNull {
        it?.toPlayer()
    }
}

internal fun DetailsUserFragment.toPlayerDetails(): PlayerDetails {

    val ageInYears = Period.between(
        OffsetDateTime.parse(birthDate.toString()).toLocalDate(),
        LocalDate.now()
    ).years
    return PlayerDetails(
        playerUid = firebaseUserId,
        playerPhotoUrl = avatar,
        age = ageInYears,
        playerName = name,
        timeAvailability = availability,
        contact = listOfNotNull(
            phone?:""
        ),
        advanceLevels = skills.associate { skill ->
            Pair(skill.sport.toSport(), skill.toAdvanceLevel())
        }
    )
}

internal fun DetailsUserFragment.Skill.toAdvanceLevel(): AdvanceLevel {
    return if(nrtp != null) {
        AdvanceLevel.NRTP(nrtp)
    } else {
        AdvanceLevel.DefaultLevel(level = level?.toInt()?:-1)
    }
}

internal fun UsersQuery.Data.toPlayerDetails(): PlayerDetails? {
    return this.users?.nodes?.get(0)?.detailsUserFragment?.toPlayerDetails()
}



internal fun UsersQuery.Node.toPlayer(): Player {
    return this.detailsUserFragment.toPlayerDetails().toPlayer()
}

internal fun futureOffersOfferFilterInput(): OfferFilterInput {
    return OfferFilterInput(
        dateTime = Optional.present(
            ComparableDateTimeOperationFilterInput(
                gte = Optional.present(OffsetDateTime.now().toString())
            )
        )
    )
}

internal fun historicalOffersOfferFilterInput(): OfferFilterInput {
    return OfferFilterInput(
        dateTime = Optional.present(
            ComparableDateTimeOperationFilterInput(
                lt = Optional.present(OffsetDateTime.now().toString())
            )
        )
    )
}

internal fun OffersFilter.toOfferFilterInput(): Optional<List<OfferFilterInput>?> {
    return this.sportFilter?.let {
        Optional.present(
            listOf(
                OfferFilterInput(
                    sport = Optional.present(
                        SportTypeOperationFilterInput(
                            eq = Optional.present(it.toSportType())
                        )
                    )
                ),
                futureOffersOfferFilterInput()
            )
        )
    }?:Optional.present(listOf(futureOffersOfferFilterInput()))
}

internal fun MeetingsFilter.toOfferFilterInput(): Optional<List<OfferFilterInput>?> {
    return sportFilter?.let { filter ->
        Optional.present(
            listOf(
                OfferFilterInput(
                    sport = Optional.present(
                        SportTypeOperationFilterInput(
                            eq = Optional.present(filter.toSportType())
                        )
                    )
                )
            )
        )
    }?: Optional.absent()
}

internal fun Pair<Sport, AdvanceLevel>.toSetSkillInput(): SetSkillInput {
    val sport = first.toSportType()
    return second.let {
        SetSkillInput(
            sport = sport,
            nrtp = if(it is AdvanceLevel.NRTP) Optional.present(it.nrtpLevel) else Optional.absent(),
            level = if(it is AdvanceLevel.DefaultLevel) Optional.present(it.level.toDouble()) else Optional.absent()
        )
    }

}


internal fun Sport.toSportType(): SportType {

    return try {
        SportType.valueOf(sportId)
    }
    catch (e: IllegalArgumentException) {
        e.printStackTrace()
        SportType.RUN
    }
}

internal fun SportType.toSport(): Sport {
    return getSportFromSportId(this.name)
}
