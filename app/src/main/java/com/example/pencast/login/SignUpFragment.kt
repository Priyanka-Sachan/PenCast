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
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.pencast.MainActivity
import com.example.pencast.R
import com.example.pencast.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class SignUpFragment : Fragment() {

    lateinit var binding: FragmentSignUpBinding

    private val IMAGE_PICKER_REQUEST_CODE = 1

    var selectedPhotoUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_sign_up,
            container,
            false
        )
        binding.signUpButton.setOnClickListener {
            signUpUser()
        }
        binding.actionSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }
        binding.signUpProfile.setOnClickListener {
            val imagePickerIntent = Intent(Intent.ACTION_PICK)
            imagePickerIntent.type = "image/*"
            startActivityForResult(imagePickerIntent, IMAGE_PICKER_REQUEST_CODE)
        }
        // Inflate the layout for this fragment
        return binding.root
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
                        binding.signUpProfile.background =
                            BitmapDrawable(context?.resources, bitmap)
                    } else {
                        val source = ImageDecoder.createSource(
                            requireActivity().contentResolver,
                            selectedPhotoUri!!
                        )
                        val bitmap = ImageDecoder.decodeBitmap(source)
                        binding.signUpProfile.background =
                            BitmapDrawable(context?.resources, bitmap)
                    }
                    binding.signUpProfile.text = ""
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun signUpUser() {
        val userName = binding.signUpUsername.text.toString()
        val email = binding.signUpEmail.text.toString()
        val password = binding.signUpPassword.text.toString()
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
            "https://firebasestorage.googleapis.com/v0/b/pencast-1163e.appspot.com/o/profileImages%2FdeaultProfile.png?alt=media&token=d088380e-1465-4b3e-883b-69362271c84a"
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
        database.setValue(User(uid, binding.signUpUsername.text.toString(), imageUrl))
        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}