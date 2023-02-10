package com.pawlowski.auth

interface ILightAuthManager {
    fun isUserAuthenticated(): Boolean
    suspend fun getToken(): String?
    fun getUserPhone(): String
    fun getCurrentUserUid(): String?
}