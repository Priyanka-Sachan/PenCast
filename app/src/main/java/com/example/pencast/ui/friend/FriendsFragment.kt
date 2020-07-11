package com.example.pencast.ui.friend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.pencast.R
import com.example.pencast.databinding.FragmentFriendsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder


class FriendsFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentFriendsBinding

    private var childEventListener: ChildEventListener? = null
    private lateinit var database: DatabaseReference

    private lateinit var friendsAdapter: GroupAdapter<GroupieViewHolder>

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

        database = FirebaseDatabase.getInstance().getReference("/Users")

        friendsAdapter = GroupAdapter<GroupieViewHolder>()
        binding.friendsRecyclerView.adapter = friendsAdapter

        attachDatabaseReadListener()

        return binding.root
    }

    private fun attachDatabaseReadListener() {
        if (childEventListener == null) {
            childEventListener = object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val friend: Friend? =
                        dataSnapshot.getValue(Friend::class.java)
                    if (friend != null && friend.uid != FirebaseAuth.getInstance().uid.toString()) {
                        friendsAdapter.add(FriendItem(friend))
                        friendsAdapter.setOnItemClickListener { item, view ->
                            val userItem = item as FriendItem
                            findNavController(this@FriendsFragment).navigate(
                                FriendsFragmentDirections.actionNavigationFriendsToNavigationChat(
                                    userItem.friend
                                )
                            )
                        }
                    }
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onCancelled(databaseError: DatabaseError) {}
            }
            database.addChildEventListener(childEventListener as ChildEventListener)
        }
    }
}