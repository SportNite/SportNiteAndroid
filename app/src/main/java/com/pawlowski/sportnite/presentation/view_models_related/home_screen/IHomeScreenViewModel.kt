package com.pawlowski.sportnite.presentation.view_models_related.home_screen

import org.orbitmvi.orbit.ContainerHost


interface IHomeScreenViewModel: ContainerHost<HomeScreenUiState, HomeScreenSideEffect> {
    fun refreshData()
}