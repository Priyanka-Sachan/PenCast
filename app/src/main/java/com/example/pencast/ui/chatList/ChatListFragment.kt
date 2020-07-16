package com.example.pencast.ui.chatList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.pencast.R
import com.example.pencast.databinding.FragmentChatListBinding
import com.example.pencast.login.User

class ChatListFragment : Fragment() {

    private lateinit var binding: FragmentChatListBinding

    private lateinit var chatListViewModel: ChatListViewModel

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

        chatListViewModel = ViewModelProvider(this).get(ChatListViewModel::class.java)
        binding.chatListViewModel = chatListViewModel
        binding.lifecycleOwner = this

        val chatListAdapter = ChatListAdapter(ChatListClickListener { chatList ->
            NavHostFragment.findNavController(this@ChatListFragment).navigate(
                ChatListFragmentDirections.actionNavigationChatListToNavigationChat(
                    User(
                        chatList.uid,
                        chatList.username,
                        chatList.profileImage,
                        chatList.status
                    )
                )
            )
        })
        binding.chatListRecyclerView.adapter = chatListAdapter

        chatListViewModel.chatLists.observe(viewLifecycleOwner, Observer {
            chatListAdapter.submitList(it)
        })

        binding.newConversationButton.setOnClickListener {
            findNavController().navigate(ChatListFragmentDirections.actionNavigationChatListToNavigationFriends())
        }

        return binding.root
    }
}
