package com.pawlowski.sportnite.presentation.view_models_related.full_screen_list_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.pawlowski.models.mappers.getSportFromSportId
import com.pawlowski.models.params_models.OffersFilter
import com.pawlowski.models.GameOffer
import com.pawlowski.models.Sport
import com.pawlowski.responses.use_cases.DeleteMyOfferToAcceptUseCase
import com.pawlowski.repository.use_cases.GetPagedOffersUseCase
import com.pawlowski.responses.use_cases.RejectOfferToAcceptUseCase
import com.pawlowski.responses.use_cases.SendGameOfferToAcceptUseCase
import com.pawlowski.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class FullScreenListViewModel @Inject constructor(
    private val getPagedOffersUseCase: GetPagedOffersUseCase,
    private val deleteMyOfferToAcceptUseCase: DeleteMyOfferToAcceptUseCase,
    private val sendGameOfferToAcceptUseCase: SendGameOfferToAcceptUseCase,
    private val rejectOfferToAcceptUseCase: RejectOfferToAcceptUseCase,
    savedStateHandle: SavedStateHandle
): IFullScreenListViewModel, ViewModel() {
    private val dataType = FullScreenDataType.getTypeFromString(savedStateHandle.get<String>("dataType")!!)
    private val sportFilter: Sport? = savedStateHandle.get<String>("sportFilter")?.let {
        getSportFromSportId(it)
    }
    override val dataTypeFlow = MutableStateFlow(dataType).asStateFlow()
    override val container: Container<FullScreenListUiState, FullScreenListSideEffect> =
        container(
            initialState = FullScreenListUiState.Initializing
        )

    override fun deleteMyOfferToAccept(offer: GameOffer) = intent {
        val result = deleteMyOfferToAcceptUseCase(offer.myResponseIdIfExists!!)
        result.onSuccess {
            changedOffers.update { offersList ->
                offersList.toMutableList().apply {
                    removeIf { it.offerUid == offer.offerUid }
                    add(offer.copy(myResponseIdIfExists = null))
                }
            }
            postSideEffect(FullScreenListSideEffect.ShowToastMessage(
                offerToAcceptDeletionSuccessText
            ))
        }.onError { message, _ ->
            postSideEffect(FullScreenListSideEffect.ShowToastMessage(message))
        }
    }

    override fun sendOfferToAccept(offer: GameOffer) = intent {
        val result = sendGameOfferToAcceptUseCase(offer.offerUid)
        result.onSuccess {
            changedOffers.update { offerList ->
                offerList.toMutableList().apply {
                    removeIf { it.offerUid == offer.offerUid }
                    add(offer.copy(myResponseIdIfExists = result.dataOrNull()))
                }
            }
            postSideEffect(FullScreenListSideEffect.ShowToastMessage(offerAcceptingSuccessText))
        }.onError { message, _ ->
            postSideEffect(FullScreenListSideEffect.ShowToastMessage(message))
        }
    }

    override fun rejectOfferToAccept(offerToAcceptUid: String) = intent {
        val result = rejectOfferToAcceptUseCase(offerToAcceptUid)
        result.onSuccess {
            postSideEffect(FullScreenListSideEffect.ShowToastMessage(UiText.NonTranslatable("Oferta została pomyślnie odrzucona")))
        }.onError { message, _ ->
            postSideEffect(FullScreenListSideEffect.ShowToastMessage(message))
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagedOffers = dataTypeFlow.flatMapLatest { type ->
        if(type is FullScreenDataType.OffersData)
            getPagedOffersUseCase(
                OffersFilter(
                sportFilter = sportFilter,
                myOffers = false
            )
            ).cachedIn(viewModelScope)
        else
            flowOf()
    }

    private val changedOffers = MutableStateFlow(listOf<GameOffer>())

    @OptIn(ExperimentalCoroutinesApi::class)
    override val offersFlow = changedOffers.flatMapLatest {
        pagedOffers.map { pagingData ->
            pagingData.map { offer ->
                val changedOffersValue = changedOffers.value
                changedOffersValue.firstOrNull { offer.offerUid == it.offerUid }?:offer
            }
        }
    }






}