package com.pawlowski.auth

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class LightAuthManager @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): ILightAuthManager {
    override fun isUserAuthenticated(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override suspend fun getToken(): String? {
        return firebaseAuth.currentUser?.getIdToken(true)?.await()?.token
    }

    override fun getUserPhone(): String {
        return firebaseAuth.currentUser?.phoneNumber.orEmpty()
    }

    override fun getCurrentUserUid(): String? {
        return firebaseAuth.currentUser?.uid
    }

}