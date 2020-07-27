package com.example.pencast.ui.follower

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
            findNavController().navigate(
                FollowerFragmentDirections.actionNavigationFollowerToNavigationProfile(
                    it
                )
            )
        })
        binding.followerRecyclerView.adapter = followerAdapter
        followerViewModel.users.observe(viewLifecycleOwner, Observer {
            followerAdapter.submitList(it)
        })

        return binding.root
    }
}