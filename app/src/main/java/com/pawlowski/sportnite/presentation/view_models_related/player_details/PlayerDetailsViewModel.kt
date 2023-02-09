package com.pawlowski.sportnite.presentation.view_models_related.player_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.pawlowski.players.use_cases.GetPlayerDetailsUseCase
import com.pawlowski.utils.UiData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class PlayerDetailsViewModel @Inject constructor(
    private val getPlayerDetailsUseCase: GetPlayerDetailsUseCase,
    savedStateHandle: SavedStateHandle,
): IPlayerDetailsViewModel, ViewModel() {
    override val container: Container<PlayerDetailsUiState, PlayerDetailsSideEffect> =
        container(
            initialState = PlayerDetailsUiState(
                UiData.Loading()
            ),
            onCreate = {
                observePlayerDetails()
            }
        )

    private val currentPlayerId = savedStateHandle.get<String>("playerId")!!

    private fun observePlayerDetails() = intent(registerIdling = false) {
        repeatOnSubscription {
            getPlayerDetailsUseCase(currentPlayerId).collectLatest {
                reduce {
                    state.copy(playerDetails = it)
                }
            }
        }
    }


}