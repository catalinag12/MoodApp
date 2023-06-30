package com.example.myapplication

import RegisterResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.firebase.FirebaseAuthService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class RegisterViewModel(private val firebaseAuthService: FirebaseAuthService) : ViewModel() {
    private val _registerResult: MutableLiveData<RegisterResult> = MutableLiveData()
    val registerResult: LiveData<RegisterResult> = _registerResult
    private val compositeDisposable = CompositeDisposable()

    fun register(email: String, password: String) {
        firebaseAuthService.register(email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { user ->
                    _registerResult.value = RegisterResult.Success(user)
                },
                onError = { error ->
                    _registerResult.value = RegisterResult.Error(error.message)
                }
            )
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}
