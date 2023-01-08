package com.pawlowski.sportnite.presentation.use_cases

import androidx.paging.PagingData
import com.pawlowski.sportnite.domain.models.PlayersFilter
import com.pawlowski.sportnite.presentation.models.Player
import kotlinx.coroutines.flow.Flow

fun interface GetPagedPlayersUseCase: (PlayersFilter) -> Flow<PagingData<Player>>