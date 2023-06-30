package com.example.myapplication


sealed class LogoutResult {
    object Success : LogoutResult()
    data class Error(val errorMessage: String?) : LogoutResult()
}