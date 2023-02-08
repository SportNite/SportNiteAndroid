package com.pawlowski.auth

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class LightAuthManager @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): ILightAuthManager {
    override fun isUserAuthenticated(): Boolean {
        return firebaseAuth.currentUser != null
    }

}