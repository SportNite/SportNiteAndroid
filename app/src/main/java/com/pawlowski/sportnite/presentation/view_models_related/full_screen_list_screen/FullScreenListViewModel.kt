package com.pawlowski.sportnite.presentation.view_models_related.full_screen_list_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pawlowski.sportnite.domain.AppRepository
import com.pawlowski.sportnite.domain.IAppRepository
import com.pawlowski.sportnite.presentation.use_cases.GetPagedMeetingsUseCase
import com.pawlowski.sportnite.presentation.use_cases.GetPagedOffersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class FullScreenListViewModel @Inject constructor(
    private val getPagedOffersUseCase: GetPagedOffersUseCase,
    private val getPagedMeetingsUseCase: GetPagedMeetingsUseCase,
    savedStateHandle: SavedStateHandle
): IFullScreenListViewModel, ViewModel() {
    private val dataType = FullScreenDataType.getTypeFromString(savedStateHandle.get<String>("dataType")!!)
    override val dataTypeFlow = MutableStateFlow(dataType).asStateFlow()
    override val container: Container<FullScreenListUiState, FullScreenListSideEffect> =
        container(
            initialState = FullScreenListUiState.Initializing
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    override val offersFlow = dataTypeFlow.flatMapLatest { type ->
        flow {
            if(type is FullScreenDataType.OffersData)
                emitAll(getPagedOffersUseCase().cachedIn(viewModelScope))
        }
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    override val meetingsFlow = dataTypeFlow.flatMapLatest { type ->
        flow {
            if(type is FullScreenDataType.MeetingsData)
                emitAll(getPagedMeetingsUseCase().cachedIn(viewModelScope))
        }
    }


}