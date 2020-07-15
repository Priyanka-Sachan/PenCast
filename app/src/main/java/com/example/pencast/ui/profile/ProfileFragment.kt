package com.example.pencast.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.pencast.R
import com.example.pencast.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding

    private val profileViewModel: ProfileViewModel by activityViewModels()

    private val IMAGE_PICKER_REQUEST_CODE: Int = 3

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

        binding.profileViewModel = profileViewModel


        binding.authEditStatus.setOnClickListener {
            binding.authStatus.isEnabled = true
            binding.authEditStatus.visibility = View.INVISIBLE
            binding.authSaveStatus.visibility = View.VISIBLE
        }

        binding.authStatus.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.toString() != profileViewModel.user.value!!.status)
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

        binding.authPickImage.setOnClickListener {
            val imagePickerIntent = Intent(Intent.ACTION_PICK)
            imagePickerIntent.type = "image/*"
            startActivityForResult(imagePickerIntent, IMAGE_PICKER_REQUEST_CODE)
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            if (data.data != null) {
                profileViewModel.updateProfileImage(data.data!!)
                Glide.with(this)
                    .load(data.data)
                    .into(binding.authProfileImage)
            }
        }
    }

    private fun updateStatus(status: String) {
        profileViewModel.updateStatus(status)
        binding.authEditStatus.visibility = View.VISIBLE
        binding.authStatus.isEnabled = false
        binding.authSaveStatus.setBackgroundResource(R.drawable.ic_circle_not_enabled)
        binding.authSaveStatus.visibility = View.INVISIBLE
    }
}