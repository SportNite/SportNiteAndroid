package com.pawlowski.sportnite.presentation.view_models_related.choose_advance_level_screen

import androidx.lifecycle.ViewModel
import com.pawlowski.sportnite.data.local.advance_level_updating_cache.AdvanceLevelUpdatingCache
import com.pawlowski.sportnite.presentation.models.AdvanceLevel
import com.pawlowski.sportnite.presentation.models.Sport
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ChooseAdvanceLevelScreenViewModel @Inject constructor(
    private val advanceLevelUpdatingCache: AdvanceLevelUpdatingCache
): IChooseAdvanceLevelScreenViewModel, ViewModel() {
    override fun selectLevel(level: AdvanceLevel) = intent {
        advanceLevelUpdatingCache.setAdvanceLevelOfSport(state.currentSport, level)
    }

    private var currentSportIndexState = MutableStateFlow(0)

    override fun continueClick() = intent {
        val sportsSize = advanceLevelUpdatingCache.chosenSports.value.size
        if(currentSportIndexState.value + 1 >= sportsSize) {
            saveLevels()
        }
        else
        {
            currentSportIndexState.update {
                it+1
            }
        }
    }

    private fun saveLevels() = intent {
        TODO()
    }
    override fun navigateBack() {
        if(currentSportIndexState.value  == 0) {
            //TODO: Navigate to ChooseSportsScreen
        }
        else
        {
            currentSportIndexState.update {
                it-1
            }
        }
    }

    private fun getSportForIndex(index: Int): Sport {
        return advanceLevelUpdatingCache.chosenSports.value.toList()[index].first
    }

    private fun observeCurrentSportAndChosenLevel() = intent(registerIdling = false) {
        repeatOnSubscription {
            currentSportIndexState.collectLatest { currentIndex ->
                val currentSport = getSportForIndex(currentIndex)
                reduce {
                    state.copy(currentSport = currentSport)
                }
                advanceLevelUpdatingCache.chosenSports.collectLatest {
                    reduce {
                        state.copy(
                            chosenLevel = it[currentSport],
                        )
                    }
                }
            }
        }
    }



    override val container: Container<ChooseAdvanceLevelScreenUiState, ChooseAdvanceLevelScreenSideEffect> =
        container(
            initialState = ChooseAdvanceLevelScreenUiState(
                currentSport = getSportForIndex(currentSportIndexState.value),
            )
        )

    init {
        observeCurrentSportAndChosenLevel()
    }
}