package com.pawlowski.home.view_model_related

import org.orbitmvi.orbit.ContainerHost


interface IHomeScreenViewModel: ContainerHost<HomeScreenUiState, HomeScreenSideEffect> {
    fun refreshData()
}