package com.example.pencast.ui.profile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
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
    lateinit var authSaveStatus: ImageButton
    lateinit var authEditStatus: ImageButton

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

        authEditStatus = view.findViewById(R.id.auth_edit_status)
        authEditStatus.setOnClickListener {
            authStatus.isEnabled = true
            authEditStatus.visibility = ImageButton.INVISIBLE
            authSaveStatus.visibility = ImageButton.VISIBLE
        }

        authSaveStatus = view.findViewById(R.id.auth_save_status)
        authStatus = view.findViewById(R.id.auth_status)
        authStatus.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.toString() != user.status)
                    authSaveStatus.isEnabled = true
                if (authSaveStatus.isEnabled)
                    authSaveStatus.setBackgroundResource(R.drawable.ic_circle_enabled)
                else
                    authSaveStatus.setBackgroundResource(R.drawable.ic_circle_not_enabled)
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        authSaveStatus.setOnClickListener {
            if (authStatus.text.toString().trim().isNotEmpty())
                updateStatus(authStatus.text.toString())
        }

        return view
    }

    private fun updateStatus(status: String) {
        database.child("status").setValue(status)
        authEditStatus.visibility = ImageButton.VISIBLE
        authStatus.isEnabled=false
        authSaveStatus.setBackgroundResource(R.drawable.ic_circle_not_enabled)
        authSaveStatus.visibility = ImageButton.INVISIBLE
    }

    private fun attachDatabaseListener() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                user = dataSnapshot.getValue(User::class.java)!!
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