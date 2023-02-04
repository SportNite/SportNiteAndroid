package com.pawlowski.sportnite.data.mappers

import com.apollographql.apollo3.api.Optional
import com.pawlowski.sportnite.*
import com.pawlowski.sportnite.domain.models.*
import com.pawlowski.sportnite.fragment.DetailsUserFragment
import com.pawlowski.sportnite.fragment.MediumUserFragment
import com.pawlowski.sportnite.fragment.OfferFragment
import com.pawlowski.sportnite.presentation.mappers.toPlayer
import com.pawlowski.sportnite.presentation.models.*
import com.pawlowski.sportnite.type.*
import com.pawlowski.sportnite.utils.UiDate
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.Period

fun PlayersFilter.toUserFilterInput(): Optional<UserFilterInput> {
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
        val sportFilter = sportFilter?.let {
            UserFilterInput(
                skills = Optional.present(
                    value = ListFilterInputTypeOfSkillFilterInput(
                        some = Optional.present(
                            SkillFilterInput(
                                sport = Optional.present(
                                    SportTypeOperationFilterInput(
                                        eq = Optional.present(sportFilter.toSportType())
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

fun OfferFragment.toGameOffer(userFragment: MediumUserFragment, myResponseIdIfExists: String? = null): GameOffer {
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

fun MediumUserFragment.toPlayer(): Player {
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

fun OffersQuery.Node.toGameOffer(): GameOffer {
    return this.offerFragment.toGameOffer(this.user.mediumUserFragment, this.myResponse?.responseId?.toString())
}


fun MyOffersQuery.Data.toGameOfferList(): List<GameOffer>? {
    return this.myOffers?.nodes?.map {
        it.toGameOffer()
    }
}

fun MyOffersQuery.Node.toGameOffer(): GameOffer {
    return this.offerFragment.toGameOffer(userFragment = this.user.mediumUserFragment, myResponseIdIfExists = null)
}

fun ResponsesQuery.User.toPlayer(): Player {
    return this.mediumUserFragment.toPlayer()
}

fun OffersQuery.Data.toGameOfferList(): List<GameOffer>? {
    return this.offers?.nodes?.mapNotNull {
        it?.toGameOffer()
    }
}

fun ResponsesQuery.Response.toGameOfferToAccept(): GameOfferToAccept {
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

fun ResponsesQuery.Data.toGameOfferToAcceptList(): List<GameOfferToAccept>? {
    return this.myOffers?.nodes?.flatMap { node ->
        node.responses.filter {
            it.status != ResponseStatus.APPROVED
        }.map {
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

fun UsersQuery.Data.toPlayersList(): List<Player>? {
    return this.users?.nodes?.mapNotNull {
        it?.toPlayer()
    }
}

fun DetailsUserFragment.toPlayerDetails(): PlayerDetails {

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

fun DetailsUserFragment.Skill.toAdvanceLevel(): AdvanceLevel {
    return if(nrtp != null) {
        AdvanceLevel.NRTP(nrtp)
    } else {
        AdvanceLevel.DefaultLevel(level = level?.toInt()?:-1)
    }
}

fun UsersQuery.Data.toPlayerDetails(): PlayerDetails? {
    return this.users?.nodes?.get(0)?.detailsUserFragment?.toPlayerDetails()
}



fun UsersQuery.Node.toPlayer(): Player {
    return this.detailsUserFragment.toPlayerDetails().toPlayer()
}

fun futureOffersOfferFilterInput(): OfferFilterInput {
    return OfferFilterInput(
        dateTime = Optional.present(
            ComparableDateTimeOperationFilterInput(
                gte = Optional.present(OffsetDateTime.now().toString())
            )
        )
    )
}

fun historicalOffersOfferFilterInput(): OfferFilterInput {
    return OfferFilterInput(
        dateTime = Optional.present(
            ComparableDateTimeOperationFilterInput(
                lt = Optional.present(OffsetDateTime.now().toString())
            )
        )
    )
}

fun OffersFilter.toOfferFilterInput(): Optional<List<OfferFilterInput>?> {
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

fun MeetingsFilter.toOfferFilterInput(): Optional<List<OfferFilterInput>?> {
    return sportFilter?.let {
        Optional.present(
            listOf(
                OfferFilterInput(
                    sport = Optional.present(
                        SportTypeOperationFilterInput(
                            eq = Optional.present(sportFilter.toSportType())
                        )
                    )
                )
            )
        )
    }?: Optional.absent()
}

fun Map<Sport, AdvanceLevel>.toSetSkillInput(): List<SetSkillInput> {
    return map {
        val sport = it.key.toSportType()
        SetSkillInput(
            sport = sport,
            nrtp = if(it.value is AdvanceLevel.NRTP) Optional.present((it.value as AdvanceLevel.NRTP).nrtpLevel) else Optional.absent(),
            level = if(it.value is AdvanceLevel.DefaultLevel) Optional.present((it.value as AdvanceLevel.DefaultLevel).level.toDouble()) else Optional.absent()
        )
    }
}


fun Sport.toSportType(): SportType {

    return try {
        SportType.valueOf(sportId)
    }
    catch (e: IllegalArgumentException) {
        e.printStackTrace()
        SportType.RUN
    }
}

fun SportType.toSport(): Sport {
    return getSportFromSportId(this.name)
}