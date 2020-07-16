
package com.example.pencast.ui.chatList

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatListViewModel(application: Application) : AndroidViewModel(application) {

    private var childEventListener: ChildEventListener? = null
    private var database: DatabaseReference

    private var _chatLists = MutableLiveData<MutableList<ChatList>>()
    val chatLists: LiveData<MutableList<ChatList>>
        get() = _chatLists

    private var chatList= ArrayList<ChatList>()

    init {
        val uid = FirebaseAuth.getInstance().uid
        database = FirebaseDatabase.getInstance().getReference("/Latest-Messages/$uid")
        attachDatabaseReadListener()
    }

    private fun attachDatabaseReadListener() {
        if (childEventListener == null) {
            childEventListener = object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val chat: ChatList? =
                        dataSnapshot.getValue(ChatList::class.java)
                    if (chat != null) {
                        chatList.add(chat)
                        _chatLists.value=chatList
                        Log.e("ChatListViewModel", _chatLists.value?.size.toString())
                    }
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onCancelled(databaseError: DatabaseError) {}
            }
            database.addChildEventListener(childEventListener as ChildEventListener)
        }
    }
}
