package com.pawlowski.sportnite.presentation.view_models_related.full_screen_list_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class FullScreenListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): IFullScreenListViewModel, ViewModel() {
    val dataType = FullScreenDataType.getTypeFromString(savedStateHandle.get<String>("dataType")!!)
    override val container: Container<FullScreenListUiState, FullScreenListSideEffect> =
        container(
            initialState = FullScreenListUiState.Initializing
        )

    private fun observeValues() = intent(registerIdling = false) {
        repeatOnSubscription {

        }
    }

    init {
        observeValues()
    }
}