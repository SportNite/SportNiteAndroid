package com.pawlowski.models.params_models

import com.pawlowski.utils.UiDate

data class UserUpdateInfoParams(
    val name: String,
    val photoUrl: String?,
    val birthDate: UiDate,
    val availability: String
)
