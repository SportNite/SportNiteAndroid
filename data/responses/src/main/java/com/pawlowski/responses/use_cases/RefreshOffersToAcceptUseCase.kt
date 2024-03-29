package com.pawlowski.responses.use_cases

import com.pawlowski.models.params_models.OffersFilter
import com.pawlowski.utils.Resource

fun interface RefreshOffersToAcceptUseCase: suspend (OffersFilter) -> Resource<Unit>