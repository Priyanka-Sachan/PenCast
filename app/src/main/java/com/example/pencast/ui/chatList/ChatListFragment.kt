package com.example.pencast.ui.chatList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.pencast.R
import com.example.pencast.databinding.FragmentChatListBinding
import com.example.pencast.ui.friend.Friend
import com.example.pencast.ui.friend.FriendItem
import com.example.pencast.ui.friend.FriendsFragmentDirections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class ChatListFragment : Fragment() {

    var childEventListener: ChildEventListener? = null
    private lateinit var chatListAdapter: GroupAdapter<GroupieViewHolder>
    lateinit var database: DatabaseReference
    lateinit var binding: FragmentChatListBinding

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

        val uid = FirebaseAuth.getInstance().uid

        binding.newConversationButton.setOnClickListener {
            findNavController().navigate(ChatListFragmentDirections.actionNavigationChatListToNavigationFriends())
        }

        database = FirebaseDatabase.getInstance().getReference("/Latest-Messages/$uid")

        chatListAdapter = GroupAdapter<GroupieViewHolder>()
        binding.chatListRecyclerView.adapter = chatListAdapter

        attachDatabaseReadListener()

        return binding.root
    }

    private fun attachDatabaseReadListener() {
        if (childEventListener == null) {
            childEventListener = object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val chatList: ChatList? =
                        dataSnapshot.getValue(ChatList::class.java)
                    if (chatList != null) {
                        chatListAdapter.add(ChatListItem(chatList))
                        chatListAdapter.setOnItemClickListener { item, view ->
                            val userItem = item as ChatListItem
                            NavHostFragment.findNavController(this@ChatListFragment).navigate(
                                ChatListFragmentDirections.actionNavigationChatListToNavigationChat(
                                    Friend(
                                        userItem.chatList.uid,
                                        userItem.chatList.profileImage,
                                        userItem.chatList.username
                                    )
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