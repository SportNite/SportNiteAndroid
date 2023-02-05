package com.pawlowski.models

import com.pawlowski.utils.UiText

data class Sport(
    val sportName: UiText,
    val sportIconId: Int,
    val sportBackgroundUrl: String,
    val sportId: String,
    )