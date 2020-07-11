package com.example.pencast.ui.profile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.pencast.R
import com.example.pencast.databinding.FragmentProfileBinding
import com.example.pencast.login.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding

    private lateinit var database: DatabaseReference

    private lateinit var thread: String
    lateinit var user: User

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
                Glide.with(binding.authProfileImage.context)
                    .load(user.profileImage)
                    .into(binding.authProfileImage)
                binding.authUsername.text = user.username
                binding.authStatus.setText(user.status)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("ProfileFragment", "loadPost:onCancelled", databaseError.toException())
            }
        })
    }
}