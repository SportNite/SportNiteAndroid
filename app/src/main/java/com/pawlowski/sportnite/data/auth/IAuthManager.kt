package com.pawlowski.sportnite.data.auth

import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.pawlowski.sportnite.presentation.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface IAuthManager {
    val signUpState: StateFlow<AuthResponse>
    fun authenticate(phone: String)
    fun onCodeSent(
        verificationId: String,
        token:
        PhoneAuthProvider.ForceResendingToken
    )

    fun onVerifyOtp(code: String)

    fun onVerificationCompleted(
        credential: PhoneAuthCredential
    )

    fun onVerificationFailed(exception: Exception)
    fun getUserPhone(): String
    fun getCurrentUserUid(): String?
}