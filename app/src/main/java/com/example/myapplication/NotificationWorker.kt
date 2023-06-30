package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class NotificationWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {
        // Build the notification
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Bliss is here!")
            .setContentText("Do not forget to add your feelings for today")
            .setSmallIcon(R.drawable.panda)
            .setAutoCancel(true)
            .build()

        // Show the notification
        with(NotificationManagerCompat.from(applicationContext)) {
            notify(notificationId, notification)
        }

        return Result.success()
    }

    companion object {
        private const val channelId = "daily_notification_channel"
        private const val notificationId = 1
    }
}
