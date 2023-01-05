package com.pawlowski.sportnite.presentation.use_cases

import androidx.paging.PagingData
import com.pawlowski.sportnite.presentation.models.GameOffer
import kotlinx.coroutines.flow.Flow

fun interface GetPagedOffersUseCase: () -> Flow<PagingData<GameOffer>>