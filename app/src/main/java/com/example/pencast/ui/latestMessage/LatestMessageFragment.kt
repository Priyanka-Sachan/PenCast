package com.example.pencast.ui.latestMessage

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
import com.example.pencast.databinding.FragmentLatestMessageBinding
import com.example.pencast.login.User

class LatestMessageFragment : Fragment() {

    private lateinit var binding: FragmentLatestMessageBinding

    private lateinit var latestMessageViewModel: LatestMessageViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_latest_message,
            container,
            false
        )

        latestMessageViewModel = ViewModelProvider(this).get(LatestMessageViewModel::class.java)
        binding.lifecycleOwner = this

        val latestMessageAdapter = LatestMessageAdapter(LatestMessageClickListener { it ->
            NavHostFragment.findNavController(this@LatestMessageFragment).navigate(
                LatestMessageFragmentDirections.actionNavigationLatestMessageToNavigationChat(
                    User(
                        it.uid,
                        it.username,
                        it.profileImage,
                        it.status
                    )
                )
            )
        })
        binding.latestMessageRecyclerView.adapter = latestMessageAdapter

        latestMessageViewModel.latestMessage.observe(viewLifecycleOwner, Observer {
            latestMessageAdapter.submitList(it)
        })

        binding.newConversationButton.setOnClickListener {
            findNavController().navigate(LatestMessageFragmentDirections.actionNavigationLatestMessageToNavigationFriends())
        }

        return binding.root
    }
}