package com.pawlowski.sportnite.presentation.models

import com.pawlowski.sportnite.utils.UiText

data class Sport(
    val sportName: UiText,
    val sportIconId: Int,
    val sportBackgroundUrl: String,
    val sportId: String,
    )