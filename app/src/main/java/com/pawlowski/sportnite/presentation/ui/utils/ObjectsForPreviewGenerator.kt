package com.pawlowski.sportnite.presentation.ui.utils

import com.pawlowski.sportnite.R
import com.pawlowski.sportnite.presentation.models.*
import com.pawlowski.sportnite.type.SportType
import com.pawlowski.sportnite.utils.UiDate
import com.pawlowski.sportnite.utils.UiText
import java.time.OffsetDateTime
import java.time.ZoneOffset

fun getPlayerForPreview(): Player {
    return Player(uid = "",
        name = "Wojciech Kowalski",
        photoUrl = "https://images.unsplash.com/photo-1620000617482-821324eb9a14?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTB8fHByb2ZpbGUlMjBpbWFnZXxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=600&q=60",
        advanceLevel = AdvanceLevel.NRTP(4.5),
        age = 20,
        phoneNumber = "501749153"
    )
}

fun getSportForPreview(): Sport {
    return Sport(
        sportName = UiText.NonTranslatable("Tenis ziemny"),
        sportBackgroundUrl = "https://images.unsplash.com/photo-1499510318569-1a3d67dc3976?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1364&q=80",
        sportId = "tennis",
        sportIconId = R.drawable.tennis_icon
    )
}

fun getMeetingForPreview(): Meeting {
    return Meeting(
        opponent = getPlayerForPreview(),
        sport = getSportForPreview(),
        placeOrAddress = "Błonia Sport",
        city = "Kraków",
        date = UiDate(OffsetDateTime.of(2023, 10, 22, 17, 0, 0, 0, ZoneOffset.UTC)),
        additionalNotes = "Rezerwacja 2h, na mączce",
        meetingUid = "id"
    )
}

fun getMeetingsListForPreview(): List<Meeting> {
    return listOf(
        getMeetingForPreview(),
        getMeetingForPreview(),
        getMeetingForPreview()
    )
}

fun getPlayerDetailsForPreview(): PlayerDetails {
    return PlayerDetails(
        playerName = "Mariusz Kowalski",
        playerPhotoUrl = "https://images.unsplash.com/photo-1620000617482-821324eb9a14?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTB8fHByb2ZpbGUlMjBpbWFnZXxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=600&q=60",
        advanceLevels = mapOf(Pair(getSportForPreview(), AdvanceLevel.NRTP(4.5))),
        age = 18,
        playerUid = "uid",
        contact = listOf("+48718904901", "kowalski@onet.pl"),
        timeAvailability = "Głównie wieczorami"
    )
}

fun getUserForPreview(): User {
    return User("Mariusz Kowalski", "https://images.unsplash.com/photo-1620000617482-821324eb9a14?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTB8fHByb2ZpbGUlMjBpbWFnZXxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=600&q=60", userPhoneNumber = "718948153")
}

fun getGameOfferForPreview(): GameOffer
{
    return GameOffer(
        owner = getPlayerForPreview(),
        date = UiDate(OffsetDateTime.of(2023, 10, 24, 17, 0, 0, 0, ZoneOffset.UTC)),
        placeOrAddress = "Błonia Sport",
        city = "Kraków",
        additionalNotes = "Rezerwacja 2h, na mączce",
        sport = getSportForPreview(),
        offerUid = "iddddddd"
    )
}

fun getGameOfferToAcceptForPreview(): GameOfferToAccept
{
    return GameOfferToAccept(
        offer = getGameOfferForPreview(),
        offerToAcceptUid = "offerToAcceptUid",
        from = getPlayerForPreview()
    )
}

fun getGameOfferToAcceptListForPreview(): List<GameOfferToAccept>
{
    return listOf(
        getGameOfferToAcceptForPreview(),
        getGameOfferToAcceptForPreview(),
        getGameOfferToAcceptForPreview(),
        getGameOfferToAcceptForPreview()
    )
}

fun getGameOfferListForPreview(): List<GameOffer>
{
    return listOf(
        getGameOfferForPreview(),
        getGameOfferForPreview(),
        getGameOfferForPreview(),
        getGameOfferForPreview()
    )
}