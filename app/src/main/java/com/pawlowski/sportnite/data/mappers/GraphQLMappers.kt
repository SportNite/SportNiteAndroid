package com.pawlowski.sportnite.data.mappers

import com.apollographql.apollo3.api.Optional
import com.pawlowski.sportnite.*
import com.pawlowski.sportnite.domain.models.*
import com.pawlowski.sportnite.presentation.models.*
import com.pawlowski.sportnite.presentation.ui.utils.getGameOfferForPreview
import com.pawlowski.sportnite.presentation.ui.utils.getPlayerForPreview
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

fun OffersQuery.Node.toGameOffer(): GameOffer {
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

fun OffersQuery.User.toPlayer(): Player {
    return getPlayerForPreview().copy(
        uid = this.firebaseUserId,
        name = this.name,
    )
}

fun MyOffersQuery.User.toPlayer(): Player {
    return getPlayerForPreview().copy(
        uid = this.firebaseUserId,
        name = this.name,
    )
}

fun MyOffersQuery.Data.toGameOfferList(): List<GameOffer>? {
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
        name = this.name,
    )
}

fun IncomingOffersQuery.User.toPlayer(): Player {
    val ageInYears = Period.between(
        OffsetDateTime.parse(birthDate.toString()).toLocalDate(),
        LocalDate.now()
    ).years
    return Player(
        uid = firebaseUserId,
        name = name,
        photoUrl = avatar,
        //advanceLevel = AdvanceLevel.NRTP(6.0), //TODO: Change
        age = ageInYears,
        phoneNumber = phone?:""
    )
}

fun IncomingOffersQuery.User1.toPlayer(): Player {
    val ageInYears = Period.between(
        OffsetDateTime.parse(birthDate.toString()).toLocalDate(),
        LocalDate.now()
    ).years
    return Player(
        uid = firebaseUserId,
        name = name,
        photoUrl = avatar,
        //advanceLevel = AdvanceLevel.NRTP(6.0), //TODO: Change
        age = ageInYears,
        phoneNumber = phone?:""
    )
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
        offer = getGameOfferForPreview().copy(offerUid = this.offerId.toString()) //TODO fix when backand error will be fixed
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
    val opponent = if (user.firebaseUserId != myUid)
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

fun UsersQuery.Data.toPlayerDetails(): PlayerDetails? {

    return this.users?.nodes?.get(0)?.let {
        val ageInYears = Period.between(
            OffsetDateTime.parse(it.birthDate.toString()).toLocalDate(),
            LocalDate.now()
        ).years
        PlayerDetails(
            playerUid = it.firebaseUserId,
            playerPhotoUrl = it.avatar,
            age = ageInYears,
            playerName = it.name,
            timeAvailability = it.availability,
            contact = listOfNotNull(
                it.phone?:""
            ),
            advanceLevels = it.skills.associate { skill ->
                Pair(skill.sport.toSport(), skill.toAdvanceLevel())
            }
        )
    }
}



fun UsersQuery.Node.toPlayer(): Player {
    val ageInYears = Period.between(
        OffsetDateTime.parse(birthDate.toString()).toLocalDate(),
        LocalDate.now()
    ).years
    return Player(
        uid = firebaseUserId,
        name = name,
        photoUrl = avatar,
        //advanceLevel = AdvanceLevel.NRTP(6.0), //TODO: Change
        age = ageInYears,
        phoneNumber = phone?:""
    )
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

fun MeQuery.Skill.toAdvanceLevel(): AdvanceLevel {
    return nrtp?.let {
        AdvanceLevel.NRTP(it)
    }?:level?.let {
        AdvanceLevel.DefaultLevel(it.toInt())
    }?: TODO()
}

fun UsersQuery.Skill.toAdvanceLevel(): AdvanceLevel {
    return if(nrtp != null) {
        AdvanceLevel.NRTP(nrtp)
    } else {
        AdvanceLevel.DefaultLevel(level = level?.toInt()?:-1)
    }
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
        SportType.RUN
    }
}

fun SportType.toSport(): Sport {
    return getSportFromSportId(this.name)
}