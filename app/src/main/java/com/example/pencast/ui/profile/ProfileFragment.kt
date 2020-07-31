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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.pencast.R
import com.example.pencast.databinding.FragmentProfileBinding
import com.example.pencast.login.User

class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding

    private lateinit var profileViewModel: ProfileViewModel

    private val IMAGE_PICKER_REQUEST_CODE: Int = 3
    private lateinit var profile: User

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

        val args = ProfileFragmentArgs.fromBundle(requireArguments())
        profile = args.profile

        val profileModelFactory = ProfileModelFactory(requireActivity().application, profile)
        profileViewModel =
            ViewModelProvider(this, profileModelFactory).get(ProfileViewModel::class.java)
        binding.profileViewModel = profileViewModel
        binding.lifecycleOwner = this

        othersView()

        profileViewModel.status.observe(viewLifecycleOwner, Observer {
            when (it) {
                0 -> othersView()
                1 -> followersView()
                2 -> userView()
            }
        })

        return binding.root
    }


    private fun othersView() {
        binding.profileFollow.visibility = View.VISIBLE

        binding.profileFollow.setOnClickListener {
            profileViewModel.addUserToFollow(profile)
            followersView()
        }
    }

    private fun followersView() {
        binding.profileFollow.visibility = View.GONE
        binding.profileChat.visibility = View.VISIBLE
        binding.profileChat.setOnClickListener {
            findNavController().navigate(
                ProfileFragmentDirections.actionNavigationProfileToNavigationChat(
                    profile
                )
            )
        }
    }

    private fun userView() {

        binding.authPickImage.visibility = View.VISIBLE
        binding.authEditStatus.visibility = View.VISIBLE
        binding.profileFollow.visibility = View.GONE

        profileViewModel.attachDatabaseListener()

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