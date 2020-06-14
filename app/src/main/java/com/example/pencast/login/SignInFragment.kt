package com.example.pencast.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.pencast.R
import com.example.pencast.databinding.FragmentSignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInFragment : Fragment() {

    lateinit var binding: FragmentSignInBinding

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
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
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
                        "User created successfully: ${it.result?.user?.uid}"
                    )
            }
            .addOnFailureListener {
                Toast.makeText(
                    activity,
                    "Failed to create user: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}