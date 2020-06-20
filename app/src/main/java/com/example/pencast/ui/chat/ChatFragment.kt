package com.example.pencast.ui.chat

import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.pencast.R
import com.example.pencast.ui.friend.Friend
import com.example.pencast.ui.friend.FriendItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class ChatFragment : Fragment() {

    var childEventListener: ChildEventListener? = null
    lateinit var chatAdapter: GroupAdapter<GroupieViewHolder>
    lateinit var chatRecyclerView: RecyclerView
    lateinit var args: ChatFragmentArgs
    lateinit var senderDatabase: DatabaseReference
    lateinit var receiverDatabase: DatabaseReference
    lateinit var chatMessage: EditText

    lateinit var toId: String
    lateinit var fromId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_chat, container, false)

        args = ChatFragmentArgs.fromBundle(requireArguments())
        (activity as AppCompatActivity).supportActionBar?.title = args.friend.username

        toId = args.friend.uid
        fromId = FirebaseAuth.getInstance().uid.toString()
        senderDatabase = FirebaseDatabase.getInstance().getReference("/Messages/$fromId/$toId")
        receiverDatabase = FirebaseDatabase.getInstance().getReference("/Messages/$toId/$fromId")

        val sendMessage: Button = view.findViewById(R.id.send_button)

        chatMessage = view.findViewById(R.id.chat_message)
        chatMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                sendMessage.isEnabled = charSequence.toString().trim { it <= ' ' }.isNotEmpty()
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        sendMessage.setOnClickListener {
            sendMessageToDatabase(chatMessage.text.toString())
        }

        chatAdapter = GroupAdapter<GroupieViewHolder>()
        chatRecyclerView = view.findViewById(R.id.chat_recycler_view)
        chatRecyclerView.adapter = chatAdapter

        attachSenderDatabaseReadListener()
        attachReceiverDatabaseReadListener()

        return view
    }

    private fun sendMessageToDatabase(message: String) {
        val timeStamp=SystemClock.currentThreadTimeMillis()
        val senderMessageObject = senderDatabase.child("$fromId&$toId@${timeStamp}")
        senderMessageObject.setValue(Chat(message, timeStamp))
    }

    private fun attachSenderDatabaseReadListener() {
        if (childEventListener == null) {
            childEventListener = object : ChildEventListener {

                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val chat: Chat? =
                        dataSnapshot.getValue(Chat::class.java)
                    if (chat != null) {
                        chatAdapter.add(ChatToItem(chat))
                    }
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onCancelled(databaseError: DatabaseError) {}
            }
            senderDatabase.addChildEventListener(childEventListener as ChildEventListener)
        }
    }

    private fun attachReceiverDatabaseReadListener() {
        if (childEventListener == null) {
            childEventListener = object : ChildEventListener {

                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val chat: Chat? =
                        dataSnapshot.getValue(Chat::class.java)
                    if (chat != null) {
                        chatAdapter.add(ChatFromItem(chat))
                    }
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onCancelled(databaseError: DatabaseError) {}
            }
            receiverDatabase.addChildEventListener(childEventListener as ChildEventListener)
        }
    }
}