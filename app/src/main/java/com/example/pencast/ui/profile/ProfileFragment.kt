package com.example.pencast.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.pencast.R
import com.example.pencast.login.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileFragment : Fragment() {

    lateinit var database: DatabaseReference
    lateinit var thread: String
    lateinit var user: User

    lateinit var authProfileImage: ImageView
    lateinit var authUsername: TextView
    lateinit var authStatus: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        thread = FirebaseAuth.getInstance().uid.toString()
        database = FirebaseDatabase.getInstance().getReference("/Users/$thread")
        attachDatabaseListener()

        authProfileImage = view.findViewById(R.id.auth_profile_image)
        authUsername = view.findViewById(R.id.auth_username)
        authStatus = view.findViewById(R.id.auth_status)

        return view
    }

    private fun attachDatabaseListener() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                user= dataSnapshot.getValue(User::class.java)!!
                Glide.with(authProfileImage.context)
                    .load(user.profileImage)
                    .into(authProfileImage)
                authUsername.text = user.username
                authStatus.text = user.status
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("ProfileFragment", "loadPost:onCancelled", databaseError.toException())
            }
        })
    }
}