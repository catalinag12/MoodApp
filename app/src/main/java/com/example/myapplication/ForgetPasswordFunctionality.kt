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

class ForgetPasswordViewModel(private val firebaseAuthService: FirebaseAuthService) : ViewModel() {
    private val _resetPasswordResult: MutableLiveData<ResetPasswordResult> = MutableLiveData()
    val resetPasswordResult: LiveData<ResetPasswordResult> = _resetPasswordResult
    private val compositeDisposable = CompositeDisposable()

    fun resetPassword(email: String) {
        firebaseAuthService.sendPasswordResetEmail(email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {
                    _resetPasswordResult.value = ResetPasswordResult.Success
                },
                onError = { error ->
                    _resetPasswordResult.value = ResetPasswordResult.Error(error.message)
                }
            )
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}
