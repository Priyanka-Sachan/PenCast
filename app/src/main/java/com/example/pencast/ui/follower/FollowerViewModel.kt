package com.example.pencast.ui.follower

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.example.pencast.login.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FollowerViewModel(application: Application) : AndroidViewModel(application) {

    private var childEventListener: ChildEventListener? = null
    private var database: DatabaseReference = FirebaseDatabase.getInstance().getReference("/Users")

    private var _users = MutableLiveData<MutableList<User>>()
    val users: LiveData<MutableList<User>>
        get() = _users

    private lateinit var myUser: User

    init {
        getMyUserData()
        attachDatabaseReadListener()
    }

    private fun getMyUserData() {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(getApplication())
        myUser = User(
            sharedPreferences.getString("UID", "No-Uid")!!,
            sharedPreferences.getString("USERNAME", "Guest User")!!,
            sharedPreferences.getString(
                "PROFILE_IMAGE_URL",
                "https://firebasestorage.googleapis.com/v0/b/pencast-1163e.appspot.com/o/profileImages%2FdeaultProfile.png?alt=media&token=d088380e-1465-4b3e-883b-69362271c84a"
            )!!,
            sharedPreferences.getString("STATUS", "Come join us at PenCast!!")!!
        )
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

    fun addUserToFollow(user: User) {
        database.child(myUser.uid).child("follower").child(user.uid).setValue(user)
        database.child(user.uid).child("follower").child(myUser.uid).setValue(myUser)
        Log.e("FollowerViewModel",myUser.uid)
    }
}