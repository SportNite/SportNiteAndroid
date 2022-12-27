package com.pawlowski.sportnite.presentation.view_models_related.my_meetings_screen

import com.pawlowski.sportnite.utils.UiText

sealed interface MyMeetingsScreenSideEffect {
    data class ShowToastMessage(val message: UiText): MyMeetingsScreenSideEffect
}