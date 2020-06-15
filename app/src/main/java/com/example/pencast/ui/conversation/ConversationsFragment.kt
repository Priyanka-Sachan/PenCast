package com.example.pencast.ui.conversation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pencast.R
import com.example.pencast.databinding.FragmentConversationsBinding

class ConversationsFragment : Fragment() {

    lateinit var binding:FragmentConversationsBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_conversations,
            container,
            false
        )
        binding.newConversationButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_conversations_to_friendsFragment)
        }
        return binding.root
    }
}