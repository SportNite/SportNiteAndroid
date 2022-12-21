package com.pawlowski.sportnite.presentation.use_cases

import com.pawlowski.sportnite.domain.models.AddGameOfferParams
import com.pawlowski.sportnite.utils.Resource

fun interface AddGameOfferUseCase: suspend (AddGameOfferParams) -> Resource<Unit>