package com.example.myapplication.repository

import com.example.myapplication.data.model.DiaryEntry
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable

class DiaryRepository(private val firestore: FirebaseFirestore) {

    fun saveDiaryEntry(entry: DiaryEntry): Completable {
        return Completable.create { emitter ->
            firestore.collection("diaryEntries")
                .document(entry.id.toString())
                .set(entry)
                .addOnSuccessListener { documentReference ->
                    emitter.onComplete()
                }
                .addOnFailureListener { e ->
                    emitter.onError(e)
                }
        }
    }
}

