package com.pawlowski.sportnite.presentation.view_models_related.sport_screen

import androidx.lifecycle.ViewModel
import com.pawlowski.sportnite.presentation.ui.utils.getSportForPreview
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SportScreenViewModel @Inject constructor(

): ISportScreenViewModel, ViewModel() {
    override val container: Container<SportScreenUiState, SportScreenSideEffect> = container(
        initialState = SportScreenUiState(getSportForPreview())
    )
}