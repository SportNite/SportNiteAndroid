package com.pawlowski.sportnite.presentation.view_models_related.full_screen_list_screen

sealed class FullScreenDataType(val columnsInRow: Int) {
    object MeetingsData: FullScreenDataType(2)
    object OffersData: FullScreenDataType(1)
    object UserSportsData: FullScreenDataType(2)

    companion object {
        fun getTypeFromString(codedType: String): FullScreenDataType {
            return when(codedType) {
                "Meetings" -> MeetingsData
                "Offers" -> OffersData
                "Users" -> UserSportsData
                else -> throw Exception("Unknown coded type")
            }
        }
    }
}
