package com.example.myapplication

 sealed class ResetPasswordResult {
    object Success : ResetPasswordResult()
    data class Error(val errorMessage: String?) : ResetPasswordResult()
}