package com.example.pencast.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.pencast.R
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment : Fragment() {

    lateinit var userNameEditText: EditText
    lateinit var emailEditText: EditText
    lateinit var passwordEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)
        val signInAction: TextView = view.findViewById(R.id.action_sign_in)
        userNameEditText = view.findViewById(R.id.sign_up_username)
        emailEditText = view.findViewById(R.id.sign_up_email)
        passwordEditText = view.findViewById(R.id.sign_up_password)
        val signUpButton: Button = view.findViewById(R.id.sign_up_button)
        signUpButton.setOnClickListener {
            signUpUser()
        }
        signInAction.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }
        // Inflate the layout for this fragment
        return view
    }

    private fun signUpUser() {
        val userName = userNameEditText.text.toString()
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
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