package com.example.pencast.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.example.pencast.MainActivity
import com.example.pencast.R
import com.example.pencast.databinding.FragmentSignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SignInFragment : Fragment() {

    lateinit var binding: FragmentSignInBinding

    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_sign_in,
            container,
            false
        )
        binding.signInButton.setOnClickListener {
            signInUser()
        }
        binding.actionSignUp.setOnClickListener {
            findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun signInUser() {
        val email = binding.signInEmail.text.toString()
        val password = binding.signInPassword.text.toString()
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(activity, "Please enter all fields.", Toast.LENGTH_SHORT).show()
            return
        }
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful)
                    return@addOnCompleteListener
                else if (it.isSuccessful)
                    Log.d(
                        "SignUpFragment",
                        "User signed in successfully: ${it.result?.user?.uid}"
                    )
                val database = FirebaseDatabase.getInstance().getReference("/Users/${FirebaseAuth.getInstance().uid}")
                database.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val user = dataSnapshot.getValue(User::class.java)!!
                        val sharedPreferences =
                            PreferenceManager.getDefaultSharedPreferences(activity)
                        val sharedPreferenceEditor = sharedPreferences.edit()
                        sharedPreferenceEditor.putString("UID", user.uid)
                        sharedPreferenceEditor.putString("USERNAME", user.username)
                        sharedPreferenceEditor.putString("STATUS", user.status)
                        sharedPreferenceEditor.putString("PROFILE_IMAGE_URL", user.profileImage)
                        sharedPreferenceEditor.apply()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.w(
                            "ProfileFragment",
                            "loadPost:onCancelled",
                            databaseError.toException()
                        )
                    }
                })

                val intent = Intent(activity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(
                    activity,
                    "Failed to sign in user: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}