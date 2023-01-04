package com.pawlowski.sportnite.presentation.view_models_related.full_screen_list_screen

import com.pawlowski.sportnite.utils.UiData

sealed class FullScreenListUiState {
    data class NormalState<T>(
        val data: UiData<T>,
        val dataType: FullScreenDataType
    ): FullScreenListUiState()
    object Initializing: FullScreenListUiState()
}
