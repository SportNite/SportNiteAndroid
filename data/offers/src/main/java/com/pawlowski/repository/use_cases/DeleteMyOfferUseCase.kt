package com.pawlowski.repository.use_cases

import com.pawlowski.utils.Resource

fun interface DeleteMyOfferUseCase: suspend (String) -> Resource<Unit>