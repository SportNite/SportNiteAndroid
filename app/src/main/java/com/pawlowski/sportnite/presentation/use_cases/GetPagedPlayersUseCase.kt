package com.pawlowski.sportnite.presentation.use_cases

import androidx.paging.PagingData
import com.pawlowski.sportnite.domain.models.PlayersFilter
import com.pawlowski.models.Player
import kotlinx.coroutines.flow.Flow

fun interface GetPagedPlayersUseCase: (PlayersFilter) -> Flow<PagingData<Player>>