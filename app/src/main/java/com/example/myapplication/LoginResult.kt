package com.example.myapplication

import com.google.firebase.auth.FirebaseUser

sealed class LoginResult {
    data class Success(val user: FirebaseUser) : LoginResult()
    data class Error(val errorMessage: String?) : LoginResult()
    object LoggedOut : LoginResult()
}
