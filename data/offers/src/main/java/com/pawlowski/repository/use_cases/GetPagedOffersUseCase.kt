package com.pawlowski.repository.use_cases

import androidx.paging.PagingData
import com.pawlowski.models.params_models.OffersFilter
import com.pawlowski.models.GameOffer
import kotlinx.coroutines.flow.Flow

fun interface GetPagedOffersUseCase: (OffersFilter) -> Flow<PagingData<GameOffer>>