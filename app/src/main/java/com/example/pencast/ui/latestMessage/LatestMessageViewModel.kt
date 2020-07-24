package com.example.pencast.ui.latestMessage

import android.app.Application
import android.app.NotificationManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LatestMessageViewModel(app: Application) : AndroidViewModel(app) {

    private var childEventListener: ChildEventListener? = null
    private var database: DatabaseReference

    private var _latestMessage = MutableLiveData<MutableList<LatestMessage>>()
    val latestMessage: LiveData<MutableList<LatestMessage>>
        get() = _latestMessage

    val notificationManager = ContextCompat.getSystemService(
        app,
        NotificationManager::class.java
    ) as NotificationManager


    private var latestMessageList = ArrayList<LatestMessage>()

    init {
        val uid = FirebaseAuth.getInstance().uid
        database = FirebaseDatabase.getInstance().getReference("Users/$uid/chat-room")
        attachDatabaseReadListener()
    }

    private fun attachDatabaseReadListener() {
        if (childEventListener == null) {
            childEventListener = object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val chat: LatestMessage? =
                        dataSnapshot.getValue(LatestMessage::class.java)
                    if (chat != null) {
                        latestMessageList.add(chat)
                        latestMessageList.sortedBy {
                            it.timeStamp
                        }
                        _latestMessage.value = latestMessageList
                    }
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                    val chat: LatestMessage? =
                        dataSnapshot.getValue(LatestMessage::class.java)
                    if (chat != null) {
                        val it: MutableIterator<LatestMessage> = latestMessageList.iterator()
                        while (it.hasNext()) {
                            if (it.next().uid == chat.uid) {
                                it.remove()
                            }
                        }
                        latestMessageList.sortedBy {
                            it.timeStamp
                        }
                        latestMessageList.add(chat)
                        _latestMessage.value = latestMessageList
                    }
                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onCancelled(databaseError: DatabaseError) {}
            }
            database.addChildEventListener(childEventListener as ChildEventListener)
        }
    }
}