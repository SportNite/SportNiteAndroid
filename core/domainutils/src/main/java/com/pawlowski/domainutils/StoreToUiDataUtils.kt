package com.pawlowski.domainutils

import com.dropbox.android.external.store4.ResponseOrigin
import com.dropbox.android.external.store4.StoreResponse
import com.pawlowski.utils.UiData
import com.pawlowski.utils.UiText
import com.pawlowski.utils.filteredIfDataExists
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

fun <Output> Flow<StoreResponse<Output>>.toUiData(
    isDataEmpty: (Output?) -> Boolean = { it != null }
): Flow<UiData<Output>> = flow {
    var lastData: Output? = null
    collect {
        when (it) {
            is StoreResponse.Loading -> {
                if (isDataEmpty(lastData))
                    emit(UiData.Loading())
            }
            is StoreResponse.Error -> {
                emit(
                    UiData.Error(
                        cachedData = lastData,
                        message = it.errorMessageOrNull()
                            ?.let { errorMessage -> UiText.NonTranslatable(errorMessage) })
                )
            }
            is StoreResponse.Data -> {
                lastData = it.value
                emit(
                    UiData.Success(
                        isFresh = it.origin == ResponseOrigin.Fetcher,
                        data = it.value
                    )
                )
            }
            is StoreResponse.NoNewData -> {
                //Do nothing?
            }
        }
    }
}



fun <Output> Flow<StoreResponse<List<Output>>>.toUiData(
    isDataEmpty: (List<Output>?) -> Boolean = { it != null && it.isNotEmpty() },
    filterPredicateOnListData: (Output) -> Boolean,
): Flow<UiData<List<Output>>> {
    return toUiData(isDataEmpty = isDataEmpty).map { data ->
        data.filteredIfDataExists(filterPredicateOnListData)
    }
}