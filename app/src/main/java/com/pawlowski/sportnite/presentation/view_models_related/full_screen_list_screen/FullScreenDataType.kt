package com.pawlowski.sportnite.presentation.view_models_related.full_screen_list_screen

import com.pawlowski.sportnite.utils.UiText

sealed class FullScreenDataType(val columnsInRow: Int, val name: UiText) {
    object MeetingsData: FullScreenDataType(2, UiText.NonTranslatable("Spotkania"))
    object OffersData: FullScreenDataType(1, UiText.NonTranslatable("Oferty na grę"))
    object UserSportsData: FullScreenDataType(2, UiText.NonTranslatable("Sporty"))
    object OffersToAccept: FullScreenDataType(1, UiText.NonTranslatable("Do akceptacji"))

    companion object {
        fun getTypeFromString(codedType: String): FullScreenDataType {
            return when(codedType) {
                "Meetings" -> MeetingsData
                "Offers" -> OffersData
                "Sports" -> UserSportsData
                "OffersToAccept" -> OffersToAccept
                else -> throw Exception("Unknown coded type")
            }
        }
    }
}
