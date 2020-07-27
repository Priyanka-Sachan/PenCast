package com.example.pencast.ui.follower

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pencast.login.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FollowerViewModel(application: Application) : AndroidViewModel(application) {

    private var childEventListener: ChildEventListener? = null
    private var database: DatabaseReference = FirebaseDatabase.getInstance().getReference("/Users")

    private var _users = MutableLiveData<MutableList<User>>()
    val users: LiveData<MutableList<User>>
        get() = _users

    init {
        attachDatabaseReadListener()
    }

    private fun attachDatabaseReadListener() {
        val userList = mutableListOf<User>()
        if (childEventListener == null) {
            childEventListener = object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val user: User? =
                        dataSnapshot.getValue(User::class.java)
                    if (user != null && user.uid != FirebaseAuth.getInstance().uid.toString()) {
                        userList.add(user)
                        _users.value = userList
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