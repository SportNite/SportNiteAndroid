package com.pawlowski.sportnite.presentation.use_cases

import androidx.paging.PagingData
import com.pawlowski.models.params_models.PlayersFilter
import com.pawlowski.models.Player
import kotlinx.coroutines.flow.Flow

fun interface GetPagedPlayersUseCase: (PlayersFilter) -> Flow<PagingData<Player>>