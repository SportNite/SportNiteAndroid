package com.pawlowski.models

import com.pawlowski.utils.UiDate
import com.pawlowski.utils.UiText

data class UserNotification(
    val tittle: UiText,
    val text: UiText,
    val date: UiDate
)
