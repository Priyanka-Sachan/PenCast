package com.example.pencast.ui.friend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.pencast.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder


class FriendsFragment() : Fragment() {

    var childEventListener: ChildEventListener? = null
    private lateinit var friendRecyclerView: RecyclerView
    private lateinit var friendsAdapter: GroupAdapter<GroupieViewHolder>
    lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_friends, container, false)

        database = FirebaseDatabase.getInstance().getReference("/Users")

        friendRecyclerView = view.findViewById(R.id.friends_recycler_view)
        friendsAdapter = GroupAdapter<GroupieViewHolder>()
        friendRecyclerView.adapter = friendsAdapter
        attachDatabaseReadListener()

        return view
    }

    private fun attachDatabaseReadListener() {
        if (childEventListener == null) {
            childEventListener = object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val friend: Friend? =
                        dataSnapshot.getValue(Friend::class.java)
                    if (friend != null && friend.uid!=FirebaseAuth.getInstance().uid.toString()) {
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