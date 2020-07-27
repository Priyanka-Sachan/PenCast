package com.example.pencast.ui.profile

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import com.example.pencast.login.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class ProfileViewModel(var application: Application, var profile: User) : ViewModel() {

    private var database: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("/Users")
    val uid = FirebaseAuth.getInstance().uid

    private val sharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(application)

    private var _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    init {
        _user.value = profile
    }

    fun attachDatabaseListener() {
        val uid = FirebaseAuth.getInstance().uid.toString()
        database.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val updatedUser = dataSnapshot.getValue(User::class.java)!!
                _user.value = updatedUser
                val sharedPreferencesEditor = sharedPreferences.edit()
                sharedPreferencesEditor.putString("STATUS", updatedUser.status)
                sharedPreferencesEditor.putString("PROFILE_IMAGE_URL", updatedUser.profileImage)
                sharedPreferencesEditor.apply()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("ProfileFragment", "loadPost:onCancelled", databaseError.toException())
            }
        })
    }

    fun updateProfileImage(selectedPhotoUri: Uri) {
        val storage = FirebaseStorage.getInstance().getReference("/profileImages/${uid}")
        storage.putFile(selectedPhotoUri)
            .addOnSuccessListener {
                storage.downloadUrl.addOnSuccessListener {
                    database.child("$uid/profileImage").setValue(it.toString())
                }
            }
    }

    fun updateStatus(status: String) {
        database.child("$uid/status").setValue(status)
    }

    fun addUserToFollow(user: User) {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(application)
        val myUser = User(
            sharedPreferences.getString("UID", "No-Uid")!!,
            sharedPreferences.getString("USERNAME", "Guest User")!!,
            sharedPreferences.getString(
                "PROFILE_IMAGE_URL",
                "https://firebasestorage.googleapis.com/v0/b/pencast-1163e.appspot.com/o/profileImages%2FdeaultProfile.png?alt=media&token=d088380e-1465-4b3e-883b-69362271c84a"
            )!!,
            sharedPreferences.getString("STATUS", "Come join us at PenCast!!")!!
        )
        database.child(myUser.uid).child("follower").child(user.uid).setValue(user)
        database.child(user.uid).child("follower").child(myUser.uid).setValue(myUser)
    }
}