package com.pawlowski.sportnite.presentation.use_cases

import com.pawlowski.sportnite.domain.models.OffersFilter
import com.pawlowski.utils.Resource

fun interface RefreshOffersToAcceptUseCase: suspend (OffersFilter) -> Resource<Unit>