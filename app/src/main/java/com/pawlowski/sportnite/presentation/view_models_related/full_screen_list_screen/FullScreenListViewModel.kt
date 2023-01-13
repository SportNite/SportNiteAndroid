package com.pawlowski.sportnite.presentation.view_models_related.full_screen_list_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pawlowski.sportnite.domain.AppRepository
import com.pawlowski.sportnite.domain.IAppRepository
import com.pawlowski.sportnite.presentation.use_cases.DeleteMyOfferToAcceptUseCase
import com.pawlowski.sportnite.presentation.use_cases.GetPagedMeetingsUseCase
import com.pawlowski.sportnite.presentation.use_cases.GetPagedOffersUseCase
import com.pawlowski.sportnite.presentation.use_cases.SendGameOfferToAcceptUseCase
import com.pawlowski.sportnite.presentation.view_models_related.sport_screen.SportScreenSideEffect
import com.pawlowski.sportnite.utils.offerAcceptingSuccessText
import com.pawlowski.sportnite.utils.offerToAcceptDeletionSuccessText
import com.pawlowski.sportnite.utils.onError
import com.pawlowski.sportnite.utils.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class FullScreenListViewModel @Inject constructor(
    private val getPagedOffersUseCase: GetPagedOffersUseCase,
    private val getPagedMeetingsUseCase: GetPagedMeetingsUseCase,
    private val deleteMyOfferToAcceptUseCase: DeleteMyOfferToAcceptUseCase,
    private val sendGameOfferToAcceptUseCase: SendGameOfferToAcceptUseCase,
    savedStateHandle: SavedStateHandle
): IFullScreenListViewModel, ViewModel() {
    private val dataType = FullScreenDataType.getTypeFromString(savedStateHandle.get<String>("dataType")!!)
    override val dataTypeFlow = MutableStateFlow(dataType).asStateFlow()
    override val container: Container<FullScreenListUiState, FullScreenListSideEffect> =
        container(
            initialState = FullScreenListUiState.Initializing
        )

    override fun deleteMyOfferToAccept(offerToAcceptUid: String) = intent {
        val result = deleteMyOfferToAcceptUseCase(offerToAcceptUid)
        result.onSuccess {
            postSideEffect(FullScreenListSideEffect.ShowToastMessage(offerToAcceptDeletionSuccessText))
        }.onError { message, _ ->
            postSideEffect(FullScreenListSideEffect.ShowToastMessage(message))
        }
    }

    override fun sendOfferToAccept(offerUid: String) = intent {
        val result = sendGameOfferToAcceptUseCase(offerUid)
        result.onSuccess {
            postSideEffect(FullScreenListSideEffect.ShowToastMessage(offerAcceptingSuccessText))
        }.onError { message, _ ->
            postSideEffect(FullScreenListSideEffect.ShowToastMessage(message))
        }
    }

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