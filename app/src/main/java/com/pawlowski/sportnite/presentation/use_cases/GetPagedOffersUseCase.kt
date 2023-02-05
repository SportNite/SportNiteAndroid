package com.pawlowski.sportnite.presentation.use_cases

import androidx.paging.PagingData
import com.pawlowski.sportnite.domain.models.OffersFilter
import com.pawlowski.models.GameOffer
import kotlinx.coroutines.flow.Flow

fun interface GetPagedOffersUseCase: (OffersFilter) -> Flow<PagingData<GameOffer>>