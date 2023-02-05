package com.pawlowski.sportnite.presentation.use_cases

import com.pawlowski.models.User
import kotlinx.coroutines.flow.Flow

fun interface GetInfoAboutMeUseCase: () -> Flow<User?>