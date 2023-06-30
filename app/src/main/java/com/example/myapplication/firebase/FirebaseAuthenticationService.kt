package com.example.myapplication.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Single

class FirebaseAuthService {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun login(email: String, password: String): Single<FirebaseUser> {
        return Single.create { emitter ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->
                    authResult.user?.let { emitter.onSuccess(it) }
                }
                .addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }

    fun register(email: String, password: String): Single<FirebaseUser> {
        return Single.create { emitter ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->
                    authResult.user?.let { emitter.onSuccess(it) }
                }
                .addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }

    fun sendPasswordResetEmail(email: String): Completable {
        return Completable.create { emitter ->
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    emitter.onComplete()
                }
                .addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }

    fun logout(): Completable {
        return Completable.create { emitter ->
            firebaseAuth.signOut()
            emitter.onComplete()
        }
    }
}
