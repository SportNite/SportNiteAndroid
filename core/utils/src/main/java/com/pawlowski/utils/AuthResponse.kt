package com.pawlowski.utils

sealed class AuthResponse {
    object NotInitialized : AuthResponse()
    class Loading(val message: String?) : AuthResponse()
    class Success(val message: String?) : AuthResponse()
    class Error(val exception: Throwable?) : AuthResponse()
}