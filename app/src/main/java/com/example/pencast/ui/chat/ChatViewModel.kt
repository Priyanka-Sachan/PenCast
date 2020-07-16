package com.example.pencast.ui.chat

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import com.example.pencast.login.User
import com.example.pencast.ui.chatList.ChatList
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList

class ChatViewModel(var application: Application, var receiver: User) : ViewModel() {

    private var childEventListener: ChildEventListener? = null

    private lateinit var messageDatabase: DatabaseReference
    private var latestMessageDatabase: DatabaseReference

    private lateinit var thread: String
    lateinit var sender: User

    private var _chat = MutableLiveData<MutableList<Chat>>()
    val chat: LiveData<MutableList<Chat>>
        get() = _chat

    var chats = ArrayList<Chat>()

    private var _latestChat = MutableLiveData<Chat?>()
    val latestChat: LiveData<Chat?>
        get() = _latestChat

    init {
        _latestChat.value = null
        getSenderData()
        getPath()
        latestMessageDatabase = FirebaseDatabase.getInstance().getReference("/Latest-Messages")
        attachDatabaseReadListener()
    }

    private fun getSenderData() {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(application)
        sender = User(
            sharedPreferences.getString("UID", "No-Uid")!!,
            sharedPreferences.getString("USERNAME", "Guest User")!!,
            sharedPreferences.getString(
                "PROFILE_IMAGE_URL",
                "https://firebasestorage.googleapis.com/v0/b/pencast-1163e.appspot.com/o/profileImages%2FdeaultProfile.png?alt=media&token=d088380e-1465-4b3e-883b-69362271c84a"
            )!!,
            sharedPreferences.getString("STATUS", "Come join us at PenCast!!")!!
        )
    }

    private fun getPath() {
        thread = if (receiver.uid > sender.uid)
            receiver.uid + sender.uid
        else
            sender.uid + receiver.uid
        messageDatabase = FirebaseDatabase.getInstance().getReference("/Messages/$thread")
    }

    fun sendMessageToDatabase(type: String, message: String) {
        val timeStamp = System.currentTimeMillis()
        val senderMessageObject = messageDatabase.child("${thread}@${timeStamp}")
        senderMessageObject.setValue(Chat(type, message, sender.uid, receiver.uid, timeStamp))

        val updatedMessage: String = if (type == "image")
            "Image"
        else
            message

        val latestSenderMessageObject = latestMessageDatabase.child(sender.uid).child(receiver.uid)
        latestSenderMessageObject.setValue(
            ChatList(
                receiver.uid,
                receiver.username,
                receiver.profileImage,
                receiver.status,
                updatedMessage,
                timeStamp
            )
        )
        val latestReceiverMessageObject =
            latestMessageDatabase.child(receiver.uid).child(sender.uid)
        latestReceiverMessageObject.setValue(
            ChatList(
                sender.uid,
                sender.username,
                sender.profileImage,
                sender.status,
                updatedMessage,
                timeStamp
            )
        )
    }

    fun uploadImage(selectedPhotoUri: Uri?) {
        if (selectedPhotoUri != null) {
            val filename = UUID.randomUUID().toString()
            val storage = FirebaseStorage.getInstance().getReference("/Images/${thread}/$filename")
            storage.putFile(selectedPhotoUri)
                .addOnSuccessListener {
                    storage.downloadUrl.addOnSuccessListener {
                        sendMessageToDatabase("image", it.toString())
                    }
                }
        }
    }

    private fun attachDatabaseReadListener() {
        if (childEventListener == null) {
            childEventListener = object : ChildEventListener {

                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val chat: Chat? =
                        dataSnapshot.getValue(Chat::class.java)
                    if (chat != null) {
                        chats.add(chat)
                        _chat.value = chats
                        _latestChat.value = chat
                    }
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onCancelled(databaseError: DatabaseError) {}
            }
            messageDatabase.addChildEventListener(childEventListener as ChildEventListener)
        }
    }
}