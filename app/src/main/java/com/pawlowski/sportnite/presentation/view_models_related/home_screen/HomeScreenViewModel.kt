package com.pawlowski.sportnite.presentation.view_models_related.home_screen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(

): IHomeScreenViewModel, ViewModel() {
    override val container: Container<HomeScreenUiState, HomeScreenSideEffect> = container(initialState = HomeScreenUiState())
}