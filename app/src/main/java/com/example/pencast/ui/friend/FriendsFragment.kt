package com.example.pencast.ui.friend

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.pencast.R
import com.google.firebase.database.*

class FriendsFragment() : Fragment() {

    var childEventListener: ChildEventListener? = null
    private lateinit var friendRecyclerView: RecyclerView
    lateinit var friendsAdapter: FriendsAdapter
    lateinit var database: DatabaseReference
    lateinit var friends: ArrayList<Friend>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_friends, container, false)

        database = FirebaseDatabase.getInstance().getReference("/Users")

        friendRecyclerView = view.findViewById(R.id.friends_recycler_view)
        friendsAdapter = FriendsAdapter()
        friendRecyclerView.adapter = friendsAdapter
        attachDatabaseReadListener()

        return view
    }

    private fun attachDatabaseReadListener() {
        friends = ArrayList()
        if (childEventListener == null) {
            childEventListener = object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {

                    val friend: Friend? =
                        dataSnapshot.getValue(Friend::class.java)
                    if (friend != null)
                        friends.add(friend)
                    friendsAdapter.submitList(friends)
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