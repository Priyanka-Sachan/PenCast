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

class SignInFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)
        val signUpAction: TextView = view.findViewById(R.id.action_sign_up)
        val emailEditText: EditText = view.findViewById(R.id.sign_in_email)
        val passwordEditText: EditText = view.findViewById(R.id.sign_in_password)
        val signInButton: Button = view.findViewById(R.id.sign_in_button)
        signInButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            Toast.makeText(activity, "Email: $email Password: $password", Toast.LENGTH_SHORT).show()
        }
        signUpAction.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
        // Inflate the layout for this fragment
        return view
    }
}