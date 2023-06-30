package com.example.myapplication

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import java.util.concurrent.TimeUnit

class NotificationScheduler(private val context: Context) {
    private val channelId = "daily_notification_channel"
    private val notificationId = 1

    @SuppressLint("MissingPermission")
    fun scheduleDailyNotification() {
        // Create a notification channel (required for Android Oreo and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Daily Notification Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Create an intent to launch your app when the notification is clicked
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // Add FLAG_IMMUTABLE flag
        )

        // Build the notification
        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Bliss is here!")
            .setContentText("Do not forget to add your feelings for today")
            .setSmallIcon(com.google.android.material.R.drawable.ic_m3_chip_check)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        // Schedule the notification to be shown daily at a fixed time
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        val initialDelay = 120_000L // 2 minutes
        val repeatInterval = 24L // 24 hours

        val notificationRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            repeatInterval, TimeUnit.HOURS
        )
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()


        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                "daily_notification",
                ExistingPeriodicWorkPolicy.KEEP,
                notificationRequest
            )

        // Show the notification immediately
        with(NotificationManagerCompat.from(context.applicationContext)) {
            notify(notificationId, notification)
        }
    }
}
