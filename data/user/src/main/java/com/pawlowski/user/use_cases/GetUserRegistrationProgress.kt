package com.pawlowski.user.use_cases

import com.pawlowski.user.data.RegistrationProgress
import com.pawlowski.utils.Resource

fun interface GetUserRegistrationProgress: suspend () -> Resource<RegistrationProgress>