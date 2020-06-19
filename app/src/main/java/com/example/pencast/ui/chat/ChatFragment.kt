package com.example.pencast.ui.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pencast.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class ChatFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_chat, container, false)

        val args = ChatFragmentArgs.fromBundle(requireArguments())
        (activity as AppCompatActivity).supportActionBar?.title = args.friend.username

        val chatAdapter=GroupAdapter<GroupieViewHolder>()
        val chatRecyclerView: RecyclerView = view.findViewById(R.id.chat_recycler_view)
        chatRecyclerView.adapter=chatAdapter

        chatAdapter.add(ChatFromItem(Chat(args.friend.profileImage,"Hello")))
        chatAdapter.add(ChatToItem(Chat(args.friend.profileImage,"How r u??")))
        chatAdapter.add(ChatFromItem(Chat(args.friend.profileImage,"I am fine..")))
        chatAdapter.add(ChatToItem(Chat(args.friend.profileImage,"U??")))
        chatAdapter.add(ChatFromItem(Chat(args.friend.profileImage,"laughable....")))
        chatAdapter.add(ChatToItem(Chat(args.friend.profileImage,"Hmmm... i can seee..Hmmm... i can seee..Hmmm... i can seee..Hmmm... i can seee.." +
                "Hmmm... i can seee..Hmmm... i can seee..Hmmm... i can seee..Hmmm... i can seee..Hmmm... i can seee..Hmmm... i can seee.." +
                "Hmmm... i can seee..Hmmm... i can seee..Hmmm... i can seee..Hmmm... i can seee..Hmmm... i can seee..Hmmm... i can seee..Hmmm... i can seee.." +
                "Hmmm... i can seee..Hmmm... i can seee..Hmmm... i can seee..Hmmm... i can seee..Hmmm... i can seee..Hmmm... i can seee..Hmmm... i can seee..")))
        chatAdapter.add(ChatFromItem(Chat(args.friend.profileImage,"So, what now???")))


        return view
    }
}