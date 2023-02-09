package com.pawlowski.responses.use_cases

import com.pawlowski.utils.Resource

fun interface DeleteMyOfferToAcceptUseCase: suspend (String) -> Resource<Unit>