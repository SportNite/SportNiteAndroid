package com.pawlowski.mymeetings.view_model_related

import com.pawlowski.utils.UiText

sealed interface MyMeetingsScreenSideEffect {
    data class ShowToastMessage(val message: UiText): MyMeetingsScreenSideEffect
}