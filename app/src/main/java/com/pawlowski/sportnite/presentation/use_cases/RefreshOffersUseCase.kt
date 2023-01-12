package com.pawlowski.sportnite.presentation.use_cases

import com.pawlowski.sportnite.domain.models.OffersFilter
import com.pawlowski.sportnite.utils.Resource

fun interface RefreshOffersUseCase: suspend (OffersFilter) -> Resource<Unit>