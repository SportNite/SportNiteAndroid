package com.pawlowski.auth

import android.app.Activity
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.pawlowski.utils.AuthResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AuthManager @Inject constructor(
    private val auth: FirebaseAuth,
    mainActivity: Activity
): IAuthManager {
    private val tag = this::class.java.simpleName

    private var verificationOtp: String = ""
    private var resentToken: PhoneAuthProvider.ForceResendingToken? = null

    override val signUpState: StateFlow<AuthResponse> get() = _signUpState
    private val _signUpState: MutableStateFlow<AuthResponse> =
        MutableStateFlow(AuthResponse.NotInitialized)


    private val authCallbacks = object :
        PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential:
                                             PhoneAuthCredential
        ) {
            Log.i(
                tag,
                "onVerificationCompleted: Verification completed."
            )
            _signUpState.value =
                AuthResponse.Loading(message = "")
            // Use obtained credential to sign in user
            signInWithAuthCredential(credential)
        }

        override fun onVerificationFailed(exception: FirebaseException) {
            _signUpState.value = AuthResponse.Error(exception)
        }

        override fun onCodeSent(code: String, token: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(code, token)
            verificationOtp = code
            resentToken = token
            _signUpState.value = AuthResponse.Loading(message = "Wysłano kod")
        }

    }

    private val authBuilder: PhoneAuthOptions.Builder = PhoneAuthOptions.newBuilder(auth)
        .setCallbacks(authCallbacks)
        .setActivity(mainActivity)
        .setTimeout(120L, TimeUnit.SECONDS)

    private fun signInWithAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i(tag, "signInWithAuthCredential:The sign in succeeded ")
                    _signUpState.value =
                        AuthResponse.Success(message = "success")
                } else {


                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Log.e(tag, "invalid code")
                        _signUpState.value = AuthResponse.Error(exception = task.exception)

                        return@addOnCompleteListener
                    } else {
                        _signUpState.value = AuthResponse.Error(task.exception)
                        Log.e(tag, "signInWithAuthCredential: Error ${task.exception?.message}")

                    }
                }

            }

    }


    override fun authenticate(phone: String) {
        _signUpState.value =
            AuthResponse.Loading("Kod zostanie wysłany na numer $phone")
        val options = authBuilder
            .setPhoneNumber(phone)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }


    override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
        authCallbacks.onCodeSent(verificationId, token)
    }

    override fun onVerifyOtp(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationOtp, code)
        signInWithAuthCredential(credential)
    }



    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
        authCallbacks.onVerificationCompleted(credential)
    }

    override fun onVerificationFailed(exception: Exception) {
        authCallbacks.onVerificationFailed(exception as FirebaseException)
    }

    override fun getUserPhone(): String {
        return auth.currentUser?.phoneNumber.orEmpty()
    }

    override fun getCurrentUserUid(): String? {
        return auth.currentUser?.uid
    }

    override fun signOut() {
        auth.signOut()
        _signUpState.value = AuthResponse.NotInitialized
        verificationOtp = ""
        resentToken = null
    }


}