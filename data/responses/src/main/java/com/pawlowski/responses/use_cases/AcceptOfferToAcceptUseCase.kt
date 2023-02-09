package com.pawlowski.responses.use_cases

import com.pawlowski.utils.Resource

fun interface AcceptOfferToAcceptUseCase: suspend (String) -> Resource<Unit>