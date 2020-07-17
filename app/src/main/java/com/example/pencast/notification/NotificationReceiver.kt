package com.example.pencast.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.e("onreceive:","received")
        Toast.makeText(context, "You have a new message..", Toast.LENGTH_SHORT).show();
    }
}
