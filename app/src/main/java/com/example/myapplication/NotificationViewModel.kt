package com.example.myapplication

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.model.MotivationalNotification
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class NotificationViewModel @Inject constructor() : ViewModel() {
    private lateinit var alarmManager: AlarmManager
    private lateinit var applicationContext: Context

    private val db = FirebaseFirestore.getInstance()
    private val collectionRef = db.collection("motivational_phrases")

    private lateinit  var notificationScheduler : NotificationScheduler

    fun toggleNotification(enabled: Boolean) {
        if (enabled) {
            notificationScheduler.scheduleDailyNotification()
        } else {
        }
    }
    fun initialize(context: Context) {
        applicationContext = context.applicationContext
        notificationScheduler = NotificationScheduler(applicationContext)
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    fun scheduleDailyNotification(hour: Int): Completable {
        return Completable.create { emitter ->
            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }

            val notificationIntent = Intent(applicationContext, MyNotificationReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )

            emitter.onComplete()
        }.subscribeOn(Schedulers.io())
    }

    fun getRandomMotivationalPhrase(): Single<MotivationalNotification> {
        return Single.create { emitter ->
            collectionRef.get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        val documents = querySnapshot.documents
                        val randomIndex = (0 until documents.size).random()
                        val randomDocument = documents[randomIndex]
                        val phrase = randomDocument.toObject(MotivationalNotification::class.java)
                        phrase?.let {
                            emitter.onSuccess(it)
                        } ?: emitter.onError(Exception("Failed to fetch motivational phrase"))
                    } else {
                        emitter.onError(Exception("No motivational phrases found"))
                    }
                }
                .addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }.subscribeOn(Schedulers.io())
    }

    fun testFirestoreDataRetrieval() {
        collectionRef.get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val documents = querySnapshot.documents
                    for (document in documents) {
                        val phrase = document.toObject(MotivationalNotification::class.java)
                        Log.d("FirestoreData", "Motivational phrase: ${phrase?.text}")
                    }
                } else {
                    Log.d("FirestoreData", "No motivational phrases found")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreData", "Failed to fetch motivational phrases", exception)
            }
    }


}
