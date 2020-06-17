package com.example.pencast.ui.chatList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pencast.R
import com.example.pencast.databinding.FragmentChatListBinding

class ChatListFragment : Fragment() {

    lateinit var binding:FragmentChatListBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_chat_list,
            container,
            false
        )
        binding.newConversationButton.setOnClickListener {
            findNavController().navigate(ChatListFragmentDirections.actionNavigationChatListToNavigationFriends())
        }
        return binding.root
    }
}