package com.pawlowski.sportnite.presentation.view_models_related.player_details

import androidx.lifecycle.ViewModel
import com.pawlowski.sportnite.presentation.ui.utils.getPlayerDetailsForPreview
import com.pawlowski.sportnite.utils.UiData
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class PlayerDetailsViewModel @Inject constructor(

): IPlayerDetailsViewModel, ViewModel() {
    override val container: Container<PlayerDetailsUiState, PlayerDetailsSideEffect> =
        container(
            initialState = PlayerDetailsUiState(
                UiData.Success(true, getPlayerDetailsForPreview())
            )
        )
}