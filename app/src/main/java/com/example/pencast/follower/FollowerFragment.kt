package com.example.pencast.follower

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pencast.databinding.FragmentFollowerBinding

class FollowerFragment : Fragment() {

    private lateinit var followerViewModel: FollowerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFollowerBinding.inflate(inflater)
        followerViewModel = ViewModelProvider(this).get(FollowerViewModel::class.java)
        binding.followerViewModel = followerViewModel

        val followerAdapter = FollowerAdapter(FollowerClickListener {
            Log.e("FollowerFragment","here")
            followerViewModel.addUserToFollow(it)
        })
        binding.followerRecyclerView.adapter = followerAdapter
        followerViewModel.users.observe(viewLifecycleOwner, Observer {
            followerAdapter.submitList(it)
        })

        return binding.root
    }
}