package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.firebase.FirebaseAuthService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class LoginViewModel (private val firebaseAuthService: FirebaseAuthService) : ViewModel() {

    private val _loginResult: MutableLiveData<LoginResult> = MutableLiveData()
    val loginResult: LiveData<LoginResult> = _loginResult
    private val _logoutResult: MutableLiveData<LogoutResult> = MutableLiveData()
    val logoutResult: LiveData<LogoutResult> = _logoutResult

    private val compositeDisposable = CompositeDisposable()

    fun login(email: String, password: String) {
        firebaseAuthService.login(email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { user ->
                    _loginResult.value = LoginResult.Success(user)
                },
                onError = { error ->
                    _loginResult.value = LoginResult.Error(error.message)
                }
            )
            .addTo(compositeDisposable)
    }


    fun logout() {
        firebaseAuthService.logout()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {
                    _logoutResult.value = LogoutResult.Success
                },
                onError = { error ->
                    _logoutResult.value = LogoutResult.Error(error.message)
                }
            )
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}