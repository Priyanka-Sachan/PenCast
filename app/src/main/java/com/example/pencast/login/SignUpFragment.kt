package com.example.pencast.login

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.example.pencast.MainActivity
import com.example.pencast.R
import com.example.pencast.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class SignUpFragment : Fragment() {

    lateinit var binding: FragmentSignUpBinding

    private val IMAGE_PICKER_REQUEST_CODE = 1

    private var selectedPhotoUri: Uri? = null

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
            binding.signUpLoading.visibility = View.VISIBLE
            binding.signUpPickImage.isEnabled = false
            binding.signUpButton.isEnabled = false
            binding.signUpEmail.isEnabled = false
            binding.signUpPassword.isEnabled = false
            binding.signUpUsername.isEnabled = false
            binding.actionSignIn.isEnabled = false
            signUpUser()
        }
        binding.actionSignIn.setOnClickListener {
            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
        }
        binding.signUpPickImage.setOnClickListener {
            val imagePickerIntent = Intent(Intent.ACTION_PICK)
            imagePickerIntent.type = "image/*"
            startActivityForResult(imagePickerIntent, IMAGE_PICKER_REQUEST_CODE)
        }

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
                        binding.signUpProfile.setImageBitmap(bitmap)
                    } else {
                        val source = ImageDecoder.createSource(
                            requireActivity().contentResolver,
                            selectedPhotoUri!!
                        )
                        val bitmap = ImageDecoder.decodeBitmap(source)
                        binding.signUpProfile.setImageBitmap(bitmap)
                    }
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
                if (activity != null) {
                    binding.signUpLoading.visibility = View.GONE
                    binding.signUpPickImage.isEnabled = true
                    binding.signUpButton.isEnabled = true
                    binding.signUpEmail.isEnabled = true
                    binding.signUpPassword.isEnabled = true
                    binding.signUpUsername.isEnabled = true
                    binding.actionSignIn.isEnabled = true
                    Toast.makeText(
                        activity,
                        "Failed to create user: ${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                } else
                    Log.e("SignUpFragment", "User clicked on Sign Up too many times.")
            }
    }

    private fun uploadProfileImage() {
        val uid = FirebaseAuth.getInstance().uid
        val storage = FirebaseStorage.getInstance().getReference("/profileImages/${uid}")
        if (selectedPhotoUri != null) {
            storage.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    storage.downloadUrl.addOnSuccessListener {
                        addUserToDatabase(it.toString())
                    }
                }
        } else {
            val profileImage =
                Uri.parse("android.resource://com.example.pencast/" + R.drawable.default_profile)
            storage.putFile(profileImage)
                .addOnSuccessListener {
                    storage.downloadUrl.addOnSuccessListener {
                        addUserToDatabase(it.toString())
                    }
                }
        }
    }

    private fun addUserToDatabase(imageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid.toString()
        val database = FirebaseDatabase.getInstance().getReference("/Users/$uid")
        val user = User(
            uid,
            binding.signUpUsername.text.toString(),
            imageUrl,
            "Let's RoCk at PenCast together..!."
        )
        database.setValue(user)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val sharedPreferenceEditor = sharedPreferences.edit()
        sharedPreferenceEditor.putString("UID", user.uid)
        sharedPreferenceEditor.putString("USERNAME", user.username)
        sharedPreferenceEditor.putString("STATUS", user.status)
        sharedPreferenceEditor.putString("PROFILE_IMAGE_URL", user.profileImage)
        sharedPreferenceEditor.apply()

        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}