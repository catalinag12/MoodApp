package com.example.myapplication

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.myapplication.data.model.MotivationalNotification
import com.example.myapplication.di.DaggerAppComponent
import javax.inject.Inject

class MyNotificationReceiver : BroadcastReceiver() {
    @Inject
    lateinit var notificationViewModel: NotificationViewModel

    companion object {
        private const val CHANNEL_ID = "motivational_channel_id"
        private const val NOTIFICATION_ID = 1
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("CheckResult")
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("Notificationz", "Received broadcast")

        val component = DaggerAppComponent.create()
        component.inject(this)

        notificationViewModel.initialize(context)

        notificationViewModel.getRandomMotivationalPhrase()
            .subscribe(
                { phrase ->
                    showMotivationalPhraseNotification(context, phrase)
                },
                { error ->
                    // Handle the error case
                }
            )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    private fun showMotivationalPhraseNotification(context: Context, phrase: MotivationalNotification) {
        Log.d("Notificationz", "Showing notification")

        createNotificationChannel(context)

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Motivational Phrase")
            .setContentText(phrase.text)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            Log.d("Notificationz", "Before notify")
            notify(NOTIFICATION_ID, notificationBuilder.build())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Motivational Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Motivational notifications"
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        Log.d("Notificationz", "Notification channel created successfully")
    }
}
