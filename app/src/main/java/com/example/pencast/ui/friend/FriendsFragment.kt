package com.example.pencast.ui.friend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.pencast.R
import com.example.pencast.databinding.FragmentFriendsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FriendsFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentFriendsBinding

    private lateinit var friendViewModel: FriendViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_friends,
            container,
            false
        )

        friendViewModel = ViewModelProvider(this).get(FriendViewModel::class.java)
        binding.friendViewModel = friendViewModel
        binding.lifecycleOwner = this

        val friendsAdapter = FriendsAdapter(FriendClickListener { friend ->
            findNavController(this@FriendsFragment).navigate(
                FriendsFragmentDirections.actionNavigationFriendsToNavigationChat(friend)
            )
        })
        binding.friendsRecyclerView.adapter = friendsAdapter

        friendViewModel.friends.observe(viewLifecycleOwner, Observer {
            it?.let {
                friendsAdapter.submitList(it)
            }
        })

        return binding.root
    }
}