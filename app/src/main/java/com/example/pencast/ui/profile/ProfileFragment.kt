package com.example.pencast.ui.profile

import android.app.Activity
import android.content.Intent
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
import com.google.firebase.storage.FirebaseStorage

class ProfileFragment : Fragment() {


    lateinit var binding: FragmentProfileBinding

    private lateinit var database: DatabaseReference

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var thread: String
    lateinit var user: User
    private val IMAGE_PICKER_REQUEST_CODE: Int = 3

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


        val uid = FirebaseAuth.getInstance().uid.toString()
        database = FirebaseDatabase.getInstance().getReference("/Users/$uid")
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

        binding.authPickImage.setOnClickListener {
            val imagePickerIntent = Intent(Intent.ACTION_PICK)
            imagePickerIntent.type = "image/*"
            startActivityForResult(imagePickerIntent, IMAGE_PICKER_REQUEST_CODE)
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val selectedPhotoUri = data.data
            if (selectedPhotoUri != null) {
                val uid = FirebaseAuth.getInstance().uid
                val storage = FirebaseStorage.getInstance().getReference("/profileImages/${uid}")
                storage.putFile(selectedPhotoUri)
                    .addOnSuccessListener {
                        storage.downloadUrl.addOnSuccessListener {
                            imageUrl = it.toString()
                            database.child("profileImage").setValue(imageUrl)
                        }
                    }
            }
        }
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
                sharedPreferencesEditor.putString("PROFILE_IMAGE_URL", user.profileImage)
                sharedPreferencesEditor.apply()
                Glide.with(binding.authProfileImage.context)
                    .load(sharedPreferences.getString("PROFILE_IMAGE_URL", imageUrl))
                    .into(binding.authProfileImage)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("ProfileFragment", "loadPost:onCancelled", databaseError.toException())
            }
        })
    }
}