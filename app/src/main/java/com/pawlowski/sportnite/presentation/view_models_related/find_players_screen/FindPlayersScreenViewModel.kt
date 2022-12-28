package com.pawlowski.sportnite.presentation.view_models_related.find_players_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import com.pawlowski.sportnite.presentation.models.AdvanceLevel
import com.pawlowski.sportnite.presentation.models.Player
import com.pawlowski.sportnite.presentation.models.Sport
import com.pawlowski.sportnite.presentation.ui.utils.getPlayerForPreview
import com.pawlowski.sportnite.presentation.use_cases.GetPlayersUseCase
import com.pawlowski.sportnite.utils.UiData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class FindPlayersScreenViewModel @Inject constructor(
    private val getPlayersUseCase: GetPlayersUseCase,
): IFindPlayersScreenViewModel, ViewModel() {
    override val container: Container<FindPlayersScreenUiState, FindPlayersScreenSideEffect> =
        container(
            initialState = FindPlayersScreenUiState(
                players = UiData.Success(true, listOf(getPlayerForPreview(), getPlayerForPreview(), getPlayerForPreview()))
            )
        )

    override fun changeSearchInput(newValue: String) = intent {
        reduce {
            state.copy(searchInput = newValue)
        }
    }

    private val filtersChangeFlow =  MutableSharedFlow<Boolean>(onBufferOverflow = BufferOverflow.DROP_OLDEST, replay = 1, extraBufferCapacity = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observePlayers() = intent(registerIdling = false) {
        repeatOnSubscription {
            filtersChangeFlow.onStart { emit(true) }.flatMapLatest {
                Log.d("getting", "getting")
                getPlayersUseCase(state.sportFilterInput, state.searchInput, state.advanceLevelFilterInput)
            }.collectLatest {
                reduce {
                    state.copy(players = it)
                }
            }
        }
    }
    override fun changeSportFilterInput(newValue: Sport?) = intent {
        reduce {
            state.copy(sportFilterInput = newValue)
        }
    }

    override fun changeLevelFilterInput(newValue: AdvanceLevel?) = intent {
        reduce {
            state.copy(advanceLevelFilterInput = newValue)
        }
    }

    override fun clearFilters() = intent {
        reduce {
            state.copy(searchInput = "", advanceLevelFilterInput = null, sportFilterInput = null)
        }
    }

    override fun applyFilters() = intent {
        filtersChangeFlow.emit(true)
    }

    init {
        observePlayers()
    }

}