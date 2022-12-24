package com.pawlowski.sportnite.domain.models

import com.pawlowski.sportnite.utils.UiDate

data class UserUpdateInfoParams(
    val name: String,
    val photoUrl: String?,
    val birthDate: UiDate,
    val availability: String
)
