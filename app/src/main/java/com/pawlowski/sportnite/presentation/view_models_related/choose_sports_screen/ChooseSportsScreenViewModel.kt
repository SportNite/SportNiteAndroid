package com.pawlowski.sportnite.presentation.view_models_related.choose_sports_screen

import androidx.lifecycle.ViewModel
import com.pawlowski.sportnite.data.local.advance_level_updating_cache.AdvanceLevelUpdatingCache
import com.pawlowski.sportnite.data.mappers.availableSports
import com.pawlowski.sportnite.presentation.models.Sport
import com.pawlowski.sportnite.presentation.view_models_related.choose_city_screen.ChooseCityScreenSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ChooseSportsScreenViewModel @Inject constructor(
    private val chooseLevelUpdatingCache: AdvanceLevelUpdatingCache,
): IChooseSportsScreenViewModel, ViewModel() {
    override fun changeSelectionOfSport(sport: Sport) = intent {
        reduce {
            state.copy(sports = state.sports.toMutableMap().apply {
                replace(sport, !(get(sport)?:false))
            })
        }
    }

    override fun continueClick() = intent {
        reduce {
            state.copy(
                isLoading = true
            )
        }
        chooseLevelUpdatingCache.setSportsAndClearLevels(
            sports = state.sports.mapNotNull {
                if(it.value)
                    it.key
                else
                    null
            }
        )
        postSideEffect(sideEffect = ChooseSportsScreenSideEffect.NavigateToChoseAdvanceLevelScreen)
        reduce {
            state.copy(
                isLoading = false
            )
        }
    }

    override fun navigateBack() {
        TODO("Not yet implemented")
    }

    override val container: Container<ChooseSportsScreenUiState, ChooseSportsScreenSideEffect> =
        container(
            initialState = ChooseSportsScreenUiState(
                sports = availableSports.values.associateWith { false }
            )
        )
}