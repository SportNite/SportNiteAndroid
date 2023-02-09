package com.pawlowski.sportnite.presentation.view_models_related.choose_advance_level_screen

import androidx.lifecycle.ViewModel
import com.pawlowski.sportnite.data.local.advance_level_updating_cache.AdvanceLevelUpdatingCache
import com.pawlowski.models.mappers.getAvailableLevelsForSport
import com.pawlowski.models.AdvanceLevel
import com.pawlowski.models.Sport
import com.pawlowski.user.use_cases.UpdateAdvanceLevelInfoUseCase
import com.pawlowski.utils.UiText
import com.pawlowski.utils.onError
import com.pawlowski.utils.onSuccess
import com.pawlowski.utils.skillsSuccessText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ChooseAdvanceLevelScreenViewModel @Inject constructor(
    private val advanceLevelUpdatingCache: AdvanceLevelUpdatingCache,
    private val updateAdvanceLevelInfoUseCase: UpdateAdvanceLevelInfoUseCase,
): IChooseAdvanceLevelScreenViewModel, ViewModel() {
    private var currentSportIndexState = MutableStateFlow(0)

    override val container: Container<ChooseAdvanceLevelScreenUiState, ChooseAdvanceLevelScreenSideEffect> =
        container(
            initialState = ChooseAdvanceLevelScreenUiState(
                currentSport = getSportForIndex(currentSportIndexState.value),
            ),
            onCreate = {
                observeCurrentSportAndChosenLevel()
            }
        )

    override fun selectLevel(level: AdvanceLevel) = intent {
        advanceLevelUpdatingCache.setAdvanceLevelOfSport(state.currentSport, level)
    }

    override fun continueClick() = intent {
        if(state.isLoading)
            return@intent

        reduce {
            state.copy(isLoading = true)
        }
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
        reduce {
            state.copy(isLoading = false)
        }
    }
    private fun saveLevels() = intent {
        val levels = advanceLevelUpdatingCache.chosenSports.value
        val isAnyMissing = levels.any { it.value == null }
        val levelsMapped = levels.map { Pair(it.key, it.value!!) }.toMap()
        if(!isAnyMissing) {
            updateAdvanceLevelInfoUseCase(levelsMapped).onSuccess {
                postSideEffect(ChooseAdvanceLevelScreenSideEffect.ShowToastMessage(skillsSuccessText))
                postSideEffect(ChooseAdvanceLevelScreenSideEffect.NavigateToHomeScreen)
            }.onError { message, _ ->
                postSideEffect(ChooseAdvanceLevelScreenSideEffect.ShowToastMessage(message))
            }
        }
        else {
            postSideEffect(ChooseAdvanceLevelScreenSideEffect.ShowToastMessage(UiText.NonTranslatable("Something is missing!")))
        }
    }

    override fun navigateBack() = intent {
        if(currentSportIndexState.value  == 0) {
            postSideEffect(ChooseAdvanceLevelScreenSideEffect.NavigateToChooseSportsScreen)
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
                    state.copy(currentSport = currentSport, availableLevels = getAvailableLevelsForSport(currentSport))
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


}