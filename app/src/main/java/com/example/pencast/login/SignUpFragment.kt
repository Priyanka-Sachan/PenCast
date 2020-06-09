package com.example.pencast.login

import android.os.Bundle
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

class SignUpFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)
        val signInAction: TextView = view.findViewById(R.id.action_sign_in)
        val userNameEditText: EditText = view.findViewById(R.id.sign_up_username)
        val emailEditText: EditText = view.findViewById(R.id.sign_up_email)
        val passwordEditText: EditText = view.findViewById(R.id.sign_up_password)
        val signUpButton: Button = view.findViewById(R.id.sign_up_button)
        signUpButton.setOnClickListener {
            val userName = userNameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            Toast.makeText(
                activity,
                "Username: $userName Email: $email Password: $password",
                Toast.LENGTH_SHORT
            ).show()
        }
        signInAction.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }
        // Inflate the layout for this fragment
        return view
    }
}