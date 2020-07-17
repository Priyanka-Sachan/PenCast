package com.example.pencast.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import com.example.pencast.notification.App.Companion.NEW_MESSAGE_CHANNEL_ID
import com.example.pencast.MainActivity
import com.example.pencast.R
import com.example.pencast.ui.chatList.ChatList

fun NotificationManager.sendNewMessageNotification(
    latestChat: ChatList,
    applicationContext: Context
) {

    val NEW_MESSAGE_NOTIFICATION_ID = 1

    val activityIntent = Intent(applicationContext, MainActivity::class.java)
    val contentIntent =
        PendingIntent.getActivity(applicationContext, 1, activityIntent, 0)

    val broadcastIntent = Intent(applicationContext, NotificationReceiver::class.java)
    val actionIntent = PendingIntent.getBroadcast(
        applicationContext,
        NEW_MESSAGE_NOTIFICATION_ID,
        broadcastIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    //For api<26,notification attributes should also be defined here.
    val notification =
        NotificationCompat.Builder(
            applicationContext,
            NEW_MESSAGE_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(latestChat.username)
            .setContentText(latestChat.message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setColor(Color.GREEN)
            .setContentIntent(contentIntent)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .addAction(R.mipmap.ic_launcher_round, "TOAST", actionIntent)

    notify(NEW_MESSAGE_NOTIFICATION_ID, notification.build())
}

fun NotificationManager.cancelNotification() {
    cancelAll()
}
