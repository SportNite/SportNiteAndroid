package com.pawlowski.fullscreenlist.view_model_related

import com.pawlowski.utils.UiText

sealed class FullScreenDataType(val columnsInRow: Int, val name: UiText) {
    object OffersData: FullScreenDataType(1, UiText.NonTranslatable("Oferty na grę"))
    object UserSportsData: FullScreenDataType(2, UiText.NonTranslatable("Sporty"))


    companion object {
        fun getTypeFromString(codedType: String): FullScreenDataType {
            return when(codedType) {
                "Offers" -> OffersData
                "Sports" -> UserSportsData
                else -> throw Exception("Unknown coded type")
            }
        }
    }
}
