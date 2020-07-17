package com.example.pencast.notification

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.content.ContextCompat

class App : Application() {
    companion object {
        const val NEW_MESSAGE_CHANNEL_ID = "NEW_MESSAGE_CHANNEL"
        const val NEW_MESSAGE_CHANNEL_NAME = "NEW MESSAGE CHANNEL"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NEW_MESSAGE_CHANNEL_ID,
                NEW_MESSAGE_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = ContextCompat.getSystemService(
                applicationContext, NotificationManager::class.java
            ) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
