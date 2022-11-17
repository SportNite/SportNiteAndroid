package com.pawlowski.sportnite.data.auth

import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow

interface IAuthManager {
    val signUpState: MutableStateFlow<AuthResponse>
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

}