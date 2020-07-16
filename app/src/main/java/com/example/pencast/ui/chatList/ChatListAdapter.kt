package com.example.pencast.ui.chatList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pencast.databinding.CardChatListBinding

class ChatListAdapter(val chatListClickListener: ChatListClickListener) : ListAdapter<ChatList,
        ChatListAdapter.ViewHolder>(ChatListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(chatListClickListener, item)
    }

    class ViewHolder private constructor(val binding: CardChatListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(chatListClickListener: ChatListClickListener, item: ChatList) {
            binding.chatList = item
            binding.chatListClickListener = chatListClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CardChatListBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class ChatListDiffCallback : DiffUtil.ItemCallback<ChatList>() {
    override fun areItemsTheSame(oldItem: ChatList, newItem: ChatList): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: ChatList, newItem: ChatList): Boolean {
        return oldItem == newItem
    }
}

class ChatListClickListener(val chatListClickListener: (chatList: ChatList) -> Unit) {
    fun onClick(chatList: ChatList) = chatListClickListener(chatList)
}


