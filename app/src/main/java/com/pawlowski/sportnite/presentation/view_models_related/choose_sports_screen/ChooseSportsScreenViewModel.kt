package com.pawlowski.sportnite.presentation.view_models_related.choose_sports_screen

import androidx.lifecycle.ViewModel
import com.pawlowski.sportnite.data.local.advance_level_updating_cache.AdvanceLevelUpdatingCache
import com.pawlowski.models.mappers.availableSports
import com.pawlowski.models.Sport
import com.pawlowski.utils.selectMinimumOneSport
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
    override val container: Container<ChooseSportsScreenUiState, ChooseSportsScreenSideEffect> =
        container(
            initialState = ChooseSportsScreenUiState(
                sports = availableSports.values.associateWith { false }
            )
        )

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
        val chosenSports = state.sports.mapNotNull {
            if(it.value)
                it.key
            else
                null
        }
        if(chosenSports.isEmpty())
        {
            postSideEffect(ChooseSportsScreenSideEffect.ShowToastMessage(selectMinimumOneSport))
        }
        else
        {
            chooseLevelUpdatingCache.setSportsAndClearLevels(
                sports = chosenSports
            )
            postSideEffect(sideEffect = ChooseSportsScreenSideEffect.NavigateToChoseAdvanceLevelScreen)
        }

        reduce {
            state.copy(
                isLoading = false
            )
        }
    }

    override fun navigateBack() {
        TODO("Not yet implemented")
    }
}