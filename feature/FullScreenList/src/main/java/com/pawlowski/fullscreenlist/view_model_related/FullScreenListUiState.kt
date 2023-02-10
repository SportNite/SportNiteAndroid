package com.pawlowski.fullscreenlist.view_model_related

import com.pawlowski.utils.UiData

sealed class FullScreenListUiState {
    data class NormalState<T>(
        val data: UiData<T>,
        val dataType: FullScreenDataType
    ): FullScreenListUiState()
    object Initializing: FullScreenListUiState()
}
