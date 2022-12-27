package com.pawlowski.sportnite.presentation.use_cases

import com.pawlowski.sportnite.utils.Resource

fun interface DeleteMyOfferUseCase: suspend (String) -> Resource<Unit>