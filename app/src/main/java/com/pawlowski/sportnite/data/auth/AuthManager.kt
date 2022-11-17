package com.pawlowski.sportnite.data.auth

import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.pawlowski.sportnite.MainActivity
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthManager @Inject constructor(
    private val auth: FirebaseAuth,
    mainActivity: MainActivity
): IAuthManager {
    private val TAG = this::class.java.simpleName

    var verificationOtp: String = ""
    var resentToken: PhoneAuthProvider.ForceResendingToken? = null
    override val signUpState: MutableStateFlow<AuthResponse> =
        MutableStateFlow(AuthResponse.NotInitialized)


    private val authCallbacks = object :
        PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential:
                                             PhoneAuthCredential
        ) {
            Log.i(
                TAG,
                "onVerificationCompleted: Verification completed."
            )
            signUpState.value =
                AuthResponse.Loading(message = "")
            // Use obtained credential to sign in user
            signInWithAuthCredential(credential)
        }

        override fun onVerificationFailed(exception: FirebaseException) {
            when (exception) {
                is FirebaseAuthInvalidCredentialsException -> {
                    signUpState.value =
                        AuthResponse.Error(exception)

                }
                is FirebaseTooManyRequestsException -> {
                    signUpState.value =
                        AuthResponse.Error(exception)

                }
                else -> {
                    signUpState.value = AuthResponse.Error(exception)

                }
            }

        }

        override fun onCodeSent(code: String, token:
        PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(code, token)
            verificationOtp = code
            resentToken = token
            signUpState.value = AuthResponse.Loading(message =
            "Wysłano kod")
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
                    Log.i(TAG, "signInWithAuthCredential:The sign in succeeded ")
                    signUpState.value =
                        AuthResponse.Success(message = "success")
                } else {


                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Log.e(TAG, "invalid code")
                        signUpState.value =
                            AuthResponse.Error(exception = task.exception)

                        return@addOnCompleteListener
                    } else {
                        signUpState.value = AuthResponse.Error(task.exception)
                        Log.e(TAG, "signInWithAuthCredential: Error ${task.exception?.message}")

                    }
                }

            }

    }


    override fun authenticate(phone: String) {
        signUpState.value =
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
}