package com.example.pencast.ui.chat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pencast.R
import com.example.pencast.databinding.FragmentChatBinding

class ChatFragment : Fragment() {

    lateinit var binding: FragmentChatBinding

    lateinit var chatViewModel: ChatViewModel
    lateinit var chatViewModelFactory: ChatViewModelFactory

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var args: ChatFragmentArgs

    private val IMAGE_PICKER_REQUEST_CODE: Int = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_chat,
            container,
            false
        )

        args = ChatFragmentArgs.fromBundle(requireArguments())
        (activity as AppCompatActivity).supportActionBar?.title = args.friend.username

        chatViewModelFactory = ChatViewModelFactory(requireActivity().application, args.friend)
        chatViewModel = ViewModelProvider(this, chatViewModelFactory).get(ChatViewModel::class.java)
        binding.chatViewModel = chatViewModel
        binding.lifecycleOwner = this

        chatAdapter = ChatAdapter(chatViewModel.sender.profileImage,chatViewModel.receiver.profileImage)
        binding.chatRecyclerView.adapter = chatAdapter

        binding.chatMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                binding.sendButton.isEnabled = charSequence.toString().trim().isNotEmpty()
                if (binding.sendButton.isEnabled)
                    binding.sendButton.setBackgroundResource(R.drawable.ic_circle_enabled)
                else
                    binding.sendButton.setBackgroundResource(R.drawable.ic_circle_not_enabled)
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        binding.sendButton.setOnClickListener {
            if (binding.chatMessage.text.toString().trim().isNotEmpty()) {
                chatViewModel.sendMessageToDatabase("text", binding.chatMessage.text.toString())
                binding.chatMessage.setText("")
            }
        }

        chatViewModel.chat.observe(viewLifecycleOwner, Observer {
            chatAdapter.submitList(it)
        })

        binding.chatPickImage.setOnClickListener {
            val imagePickerIntent = Intent(Intent.ACTION_PICK)
            imagePickerIntent.type = "image/*"
            startActivityForResult(imagePickerIntent, IMAGE_PICKER_REQUEST_CODE)
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val selectedPhotoUri = data.data
            chatViewModel.uploadImage(selectedPhotoUri)
        }
    }
}