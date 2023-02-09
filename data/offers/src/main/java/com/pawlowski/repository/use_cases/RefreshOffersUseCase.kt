package com.pawlowski.repository.use_cases

import com.pawlowski.models.params_models.OffersFilter
import com.pawlowski.utils.Resource

fun interface RefreshOffersUseCase: suspend (OffersFilter) -> Resource<Unit>