package com.example.pencast.ui.friend

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FriendViewModel(application: Application) : AndroidViewModel(application) {

    private var childEventListener: ChildEventListener? = null
    private var database: DatabaseReference = FirebaseDatabase.getInstance().getReference("/Users")

    private var _friends = MutableLiveData<MutableList<Friend>>()
    val friends: LiveData<MutableList<Friend>>
        get() = _friends

    fun attachDatabaseReadListener() {
        val friendList = mutableListOf<Friend>()
        if (childEventListener == null) {
            childEventListener = object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val friend: Friend? =
                        dataSnapshot.getValue(Friend::class.java)
                    if (friend != null && friend.uid != FirebaseAuth.getInstance().uid.toString()) {
                        friendList.add(friend)
                        _friends.value = friendList
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