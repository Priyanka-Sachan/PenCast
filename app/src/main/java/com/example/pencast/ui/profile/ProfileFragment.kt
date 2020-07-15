package com.example.pencast.ui.profile

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.example.pencast.R
import com.example.pencast.databinding.FragmentProfileBinding
import com.example.pencast.login.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding

    private lateinit var database: DatabaseReference

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var thread: String
    lateinit var user: User

    var imageUrl =
        "https://firebasestorage.googleapis.com/v0/b/pencast-1163e.appspot.com/o/profileImages%2FdeaultProfile.png?alt=media&token=d088380e-1465-4b3e-883b-69362271c84a"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_profile,
            container,
            false
        )

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        Glide.with(binding.authProfileImage.context)
            .load(sharedPreferences.getString("PROFILE_IMAGE_URL", imageUrl))
            .into(binding.authProfileImage)
        binding.authUsername.text = sharedPreferences.getString("USERNAME", "Guest User")
        binding.authStatus.setText(
            sharedPreferences.getString(
                "STATUS",
                "Come join us at PenCast!!"
            )
        )


        thread = FirebaseAuth.getInstance().uid.toString()
        database = FirebaseDatabase.getInstance().getReference("/Users/$thread")
        attachDatabaseListener()

        binding.authEditStatus.setOnClickListener {
            binding.authStatus.isEnabled = true
            binding.authEditStatus.visibility = View.INVISIBLE
            binding.authSaveStatus.visibility = View.VISIBLE
        }

        binding.authStatus.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.toString() != user.status)
                    binding.authSaveStatus.isEnabled = true
                if (binding.authSaveStatus.isEnabled)
                    binding.authSaveStatus.setBackgroundResource(R.drawable.ic_circle_enabled)
                else
                    binding.authSaveStatus.setBackgroundResource(R.drawable.ic_circle_not_enabled)
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        binding.authSaveStatus.setOnClickListener {
            if (binding.authStatus.text.toString().trim().isNotEmpty())
                updateStatus(binding.authStatus.text.toString())
        }

        return binding.root
    }

    private fun updateStatus(status: String) {
        database.child("status").setValue(status)
        binding.authEditStatus.visibility = View.VISIBLE
        binding.authStatus.isEnabled = false
        binding.authSaveStatus.setBackgroundResource(R.drawable.ic_circle_not_enabled)
        binding.authSaveStatus.visibility = View.INVISIBLE
    }

    private fun attachDatabaseListener() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                user = dataSnapshot.getValue(User::class.java)!!
                val sharedPreferencesEditor = sharedPreferences.edit()
                sharedPreferencesEditor.putString("STATUS", user.status)
                sharedPreferencesEditor.apply()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("ProfileFragment", "loadPost:onCancelled", databaseError.toException())
            }
        })
    }
}