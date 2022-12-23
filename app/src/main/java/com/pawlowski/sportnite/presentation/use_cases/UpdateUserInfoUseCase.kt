package com.pawlowski.sportnite.presentation.use_cases

import com.pawlowski.sportnite.domain.models.UserUpdateInfoParams
import com.pawlowski.sportnite.utils.Resource

fun interface UpdateUserInfoUseCase: suspend (UserUpdateInfoParams) -> Resource<Unit>