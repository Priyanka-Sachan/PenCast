package com.example.pencast.ui.chat

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.pencast.R
import com.example.pencast.databinding.FragmentChatBinding
import com.example.pencast.ui.chatList.ChatList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class ChatFragment : Fragment() {

    lateinit var binding: FragmentChatBinding

    private var childEventListener: ChildEventListener? = null
    private lateinit var messageDatabase: DatabaseReference
    private lateinit var latestMessageDatabase: DatabaseReference

    private lateinit var chatAdapter: GroupAdapter<GroupieViewHolder>
    private lateinit var args: ChatFragmentArgs

    private lateinit var toId: String
    private lateinit var fromId: String
    private lateinit var thread: String
    private val profileImage =
        "https://firebasestorage.googleapis.com/v0/b/pencast-1163e.appspot.com" +
                "/o/profileImages%2FdeaultProfile.png?alt=media&token=d088380e-1465-4b3e-883b-69362271c84a"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_chat,
            container,
            false
        )

        args = ChatFragmentArgs.fromBundle(requireArguments())
        (activity as AppCompatActivity).supportActionBar?.title = args.friend.username
        toId = args.friend.uid
        fromId = FirebaseAuth.getInstance().uid.toString()
        thread = if (toId > fromId)
            toId + fromId
        else
            fromId + toId

        messageDatabase = FirebaseDatabase.getInstance().getReference("/Messages/$thread")
        latestMessageDatabase = FirebaseDatabase.getInstance().getReference("/Latest-Messages")

        binding.chatMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                binding.sendButton.isEnabled = charSequence.toString().trim().isNotEmpty()
                if (binding.sendButton.isEnabled)
                    binding.sendButton.setBackgroundResource(R.drawable.ic_circle_enabled)
                else
                    binding.sendButton.setBackgroundResource(R.drawable.ic_circle_not_enabled)
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        binding.sendButton.setOnClickListener {
            if (binding.chatMessage.text.toString().trim().isNotEmpty()) {
                sendMessageToDatabase(binding.chatMessage.text.toString())
                binding.chatMessage.setText("")
            }
        }

        chatAdapter = GroupAdapter<GroupieViewHolder>()
        binding.chatRecyclerView.adapter = chatAdapter

        attachDatabaseReadListener()

        return binding.root
    }

    private fun sendMessageToDatabase(message: String) {
        val timeStamp = System.currentTimeMillis()
        val senderMessageObject = messageDatabase.child("${thread}@${timeStamp}")
        senderMessageObject.setValue(Chat("text", message, fromId, toId, timeStamp))
        val latestSenderMessageObject = latestMessageDatabase.child(fromId).child(toId)
        latestSenderMessageObject.setValue(
            ChatList(
                toId,
                args.friend.username,
                args.friend.profileImage,
                args.friend.status,
                message,
                timeStamp
            )
        )
        val latestReceiverMessageObject = latestMessageDatabase.child(toId).child(fromId)
        latestReceiverMessageObject.setValue(
            ChatList(
                fromId,
                "Shared preferences username",
                profileImage,
                "Shared preferences status",
                message,
                timeStamp
            )
        )
    }

    private fun attachDatabaseReadListener() {
        if (childEventListener == null) {
            childEventListener = object : ChildEventListener {

                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val chat: Chat? =
                        dataSnapshot.getValue(Chat::class.java)
                    if (chat != null) {
                        if (chat.type == "text") {
                            if (chat.senderId == fromId)
                                chatAdapter.add(ChatToItem(chat, profileImage))
                            else
                                chatAdapter.add(
                                    ChatFromItem(
                                        chat,
                                        args.friend.profileImage
                                    )
                                ) //Will be changed by shared preference later on
                        }
                    }
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onCancelled(databaseError: DatabaseError) {}
            }
            messageDatabase.addChildEventListener(childEventListener as ChildEventListener)
        }
    }
}