package com.pawlowski.sportnite.presentation.use_cases

import com.pawlowski.models.params_models.AddGameOfferParams
import com.pawlowski.utils.Resource

fun interface AddGameOfferUseCase: suspend (AddGameOfferParams) -> Resource<Unit>