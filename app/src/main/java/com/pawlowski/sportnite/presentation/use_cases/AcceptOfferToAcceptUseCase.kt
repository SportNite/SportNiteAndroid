package com.pawlowski.sportnite.presentation.use_cases

import com.pawlowski.sportnite.utils.Resource

fun interface AcceptOfferToAcceptUseCase: suspend (String) -> Resource<Unit>