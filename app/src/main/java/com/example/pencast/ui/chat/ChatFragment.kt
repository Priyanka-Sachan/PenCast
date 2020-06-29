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
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.pencast.R
import com.example.pencast.ui.chatList.ChatList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class ChatFragment : Fragment() {

    var childEventListener: ChildEventListener? = null
    lateinit var chatAdapter: GroupAdapter<GroupieViewHolder>
    lateinit var chatRecyclerView: RecyclerView
    lateinit var args: ChatFragmentArgs
    lateinit var messageDatabase: DatabaseReference
    lateinit var latestMessageDatabase: DatabaseReference
    lateinit var chatMessage: EditText

    lateinit var toId: String
    lateinit var fromId: String
    lateinit var thread: String

    lateinit var profileImage: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_chat, container, false)
        profileImage = "https://firebasestorage.googleapis.com/v0/b/pencast-1163e.appspot.com" +
                "/o/profileImages%2FdeaultProfile.png?alt=media&token=d088380e-1465-4b3e-883b-69362271c84a"
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

        val sendMessage: ImageButton = view.findViewById(R.id.send_button)

        chatMessage = view.findViewById(R.id.chat_message)
        chatMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                sendMessage.isEnabled = charSequence.toString().trim().isNotEmpty()
                if (sendMessage.isEnabled)
                    sendMessage.setBackgroundResource(R.drawable.ic_circle_enabled)
                else
                    sendMessage.setBackgroundResource(R.drawable.ic_circle_not_enabled)
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        sendMessage.setOnClickListener {
            if (chatMessage.text.toString().trim().isNotEmpty()) {
                sendMessageToDatabase(chatMessage.text.toString())
                chatMessage.setText("")
            }
        }

        chatAdapter = GroupAdapter<GroupieViewHolder>()
        chatRecyclerView = view.findViewById(R.id.chat_recycler_view)
        chatRecyclerView.adapter = chatAdapter

        attachDatabaseReadListener()

        return view
    }

    private fun sendMessageToDatabase(message: String) {
        val timeStamp = System.currentTimeMillis()
        val senderMessageObject = messageDatabase.child("${thread}@${timeStamp}")
        senderMessageObject.setValue(Chat(message, fromId, toId, timeStamp))
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

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onCancelled(databaseError: DatabaseError) {}
            }
            messageDatabase.addChildEventListener(childEventListener as ChildEventListener)
        }
    }
}