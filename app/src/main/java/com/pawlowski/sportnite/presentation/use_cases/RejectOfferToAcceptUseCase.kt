package com.pawlowski.sportnite.presentation.use_cases

import com.pawlowski.utils.Resource

fun interface RejectOfferToAcceptUseCase: suspend (String) -> Resource<Unit>