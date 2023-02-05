package com.pawlowski.sportnite.presentation.use_cases

import com.pawlowski.models.params_models.UserUpdateInfoParams
import com.pawlowski.utils.Resource

fun interface UpdateUserInfoUseCase: suspend (UserUpdateInfoParams) -> Resource<Unit>