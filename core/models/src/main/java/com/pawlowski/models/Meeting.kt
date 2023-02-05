package com.pawlowski.models

import com.pawlowski.utils.UiDate

data class Meeting(
    val opponent: Player,
    val sport: Sport,
    val placeOrAddress: String,
    val city: String,
    val date: UiDate,
    val additionalNotes: String,
    val meetingUid: String
)
