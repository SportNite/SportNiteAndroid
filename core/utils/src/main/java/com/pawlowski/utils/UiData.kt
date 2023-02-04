package com.pawlowski.utils

sealed class UiData<T> {
    class Loading<T>: UiData<T>()
    data class Error<T>(val cachedData: T? = null, val message: UiText? = null): UiData<T>()
    data class Success<T>(val isFresh: Boolean, val data: T): UiData<T>()
}
