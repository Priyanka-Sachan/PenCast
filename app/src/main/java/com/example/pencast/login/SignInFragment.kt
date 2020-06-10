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

class SignInFragment : Fragment() {

    lateinit var emailEditText: EditText
    lateinit var passwordEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)
        val signUpAction: TextView = view.findViewById(R.id.action_sign_up)
        emailEditText = view.findViewById(R.id.sign_in_email)
        passwordEditText = view.findViewById(R.id.sign_in_password)
        val signInButton: Button = view.findViewById(R.id.sign_in_button)
        signInButton.setOnClickListener {
            signInUser()
        }
        signUpAction.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
        // Inflate the layout for this fragment
        return view
    }

    private fun signInUser() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
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