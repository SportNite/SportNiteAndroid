package com.pawlowski.utils

sealed class Resource<T> {
    class Success<T>(val data: T): Resource<T>()
    class Error<T>(val message: UiText, val data: T? = null): Resource<T>()
}