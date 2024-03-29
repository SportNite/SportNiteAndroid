package com.pawlowski.findplayerrs.view_model_related

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.pawlowski.models.params_models.PlayersFilter
import com.pawlowski.models.AdvanceLevel
import com.pawlowski.models.Sport
import com.pawlowski.players.use_cases.GetPagedPlayersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class FindPlayersScreenViewModel @Inject constructor(
    private val getPagedPlayersUseCase: GetPagedPlayersUseCase
): IFindPlayersScreenViewModel, ViewModel() {
    override val container: Container<FindPlayersScreenUiState, FindPlayersScreenSideEffect> =
        container(
            initialState = FindPlayersScreenUiState(
            )
        )

    private val currentAppliedFiltersFlow = MutableStateFlow(PlayersFilter())

    @OptIn(ExperimentalCoroutinesApi::class)
    override val pagedPlayers = currentAppliedFiltersFlow.flatMapLatest {
        getPagedPlayersUseCase(it)
    }.cachedIn(viewModelScope)

    override fun changeSearchInput(newValue: String) = intent {
        reduce {
            state.copy(searchInput = newValue, wereAnyFiltersChangedBeforeApply = true)
        }
    }

    override fun changeSportFilterInput(newValue: Sport?) = intent {
        reduce {
            state.copy(sportFilterInput = newValue, wereAnyFiltersChangedBeforeApply = true)
        }
    }

    override fun changeLevelFilterInput(newValue: AdvanceLevel?) = intent {
        reduce {
            state.copy(advanceLevelFilterInput = newValue, wereAnyFiltersChangedBeforeApply = true)
        }
    }

    override fun clearFilters() = intent {
        reduce {
            state.copy(searchInput = "", advanceLevelFilterInput = null, sportFilterInput = null, wereAnyFiltersChangedBeforeApply = false)
        }
        applyFilters()
    }

    override fun applyFilters() = intent {
        reduce {
            state.copy(
                wereAnyFiltersChangedBeforeApply = false,
                areAnyFiltersOn = state.sportFilterInput != null || state.searchInput.isNotEmpty() || state.advanceLevelFilterInput != null
            )
        }
        currentAppliedFiltersFlow.update {
            PlayersFilter(sportFilter = state.sportFilterInput, nameSearch = state.searchInput, level = state.advanceLevelFilterInput)
        }
    }


}