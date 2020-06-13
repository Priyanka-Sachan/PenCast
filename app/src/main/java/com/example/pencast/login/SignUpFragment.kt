package com.example.pencast.login

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_sign_up.*
import java.util.*

class SignUpFragment : Fragment() {

    private val IMAGE_PICKER_REQUEST_CODE = 1

    var selectedPhotoUri: Uri? = null

    lateinit var signUpProfile: Button
    lateinit var userNameEditText: EditText
    lateinit var emailEditText: EditText
    lateinit var passwordEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)
        val signInAction: TextView = view.findViewById(R.id.action_sign_in)
        signUpProfile = view.findViewById(R.id.sign_up_profile)
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
        signUpProfile.setOnClickListener {
            val imagePickerIntent = Intent(Intent.ACTION_PICK)
            imagePickerIntent.type = "image/*"
            startActivityForResult(imagePickerIntent, IMAGE_PICKER_REQUEST_CODE)
        }
        // Inflate the layout for this fragment
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data
            try {
                selectedPhotoUri?.let {
                    if (Build.VERSION.SDK_INT < 28) {
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            activity?.contentResolver,
                            selectedPhotoUri
                        )
                        signUpProfile.background = BitmapDrawable(context?.resources, bitmap)
                    } else {
                        val source = ImageDecoder.createSource(
                            requireActivity().contentResolver,
                            selectedPhotoUri!!
                        )
                        val bitmap = ImageDecoder.decodeBitmap(source)
                        signUpProfile.background = BitmapDrawable(context?.resources, bitmap)
                    }
                    signUpProfile.text = ""
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun signUpUser() {
        val userName = userNameEditText.text.toString()
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        if (userName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(activity, "Please enter all fields.", Toast.LENGTH_SHORT).show()
            return
        }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful)
                    return@addOnCompleteListener
                else if (it.isSuccessful)
                    Log.d(
                        "SignUpFragment",
                        "User created successfully: ${it.result?.user?.uid}"
                    )
                uploadProfileImage()
            }
            .addOnFailureListener {
                Toast.makeText(
                    activity,
                    "Failed to create user: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun uploadProfileImage() {
        var imageUrl =
            "https://firebasestorage.googleapis.com/v0/b/pencast-1163e.appspot.com/o/profileImages%2FdeaultProfile.png"
        if (selectedPhotoUri != null) {
            val filename = UUID.randomUUID().toString()
            val storage = FirebaseStorage.getInstance().getReference("/profileImages/$filename")
            storage.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    storage.downloadUrl.addOnSuccessListener {
                        imageUrl = it.toString()
                        addUserToDatabase(it.toString())
                    }
                }
        } else
            addUserToDatabase(imageUrl)
    }

    private fun addUserToDatabase(imageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid.toString()
        val database = FirebaseDatabase.getInstance().getReference("/Users/$uid")
        database.setValue(User(uid, userNameEditText.text.toString(), imageUrl))
    }
}