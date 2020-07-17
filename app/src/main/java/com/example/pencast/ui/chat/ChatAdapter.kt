package com.example.pencast.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pencast.databinding.CardChatFromImageBinding
import com.example.pencast.databinding.CardChatFromTextBinding
import com.example.pencast.databinding.CardChatToImageBinding
import com.example.pencast.databinding.CardChatToTextBinding
import com.google.firebase.auth.FirebaseAuth

class ChatAdapter(
    private val senderProfileImage: String,
    private val receiverProfileImage: String
) :
    ListAdapter<Chat,
            RecyclerView.ViewHolder>(ChatDiffCallback()) {

    class ChatFromTextViewHolder private constructor(val binding: CardChatFromTextBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Chat, profileImage: String) {
            binding.chat = item
            binding.profileImage = profileImage
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ChatFromTextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CardChatFromTextBinding.inflate(layoutInflater, parent, false)
                return ChatFromTextViewHolder(binding)
            }
        }
    }

    class ChatToTextViewHolder private constructor(val binding: CardChatToTextBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Chat, profileImage: String) {
            binding.chat = item
            binding.profileImage = profileImage
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ChatToTextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CardChatToTextBinding.inflate(layoutInflater, parent, false)
                return ChatToTextViewHolder(binding)
            }
        }
    }

    class ChatFromImageViewHolder private constructor(val binding: CardChatFromImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Chat, profileImage: String) {
            binding.chat = item
            binding.profileImage = profileImage
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ChatFromImageViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CardChatFromImageBinding.inflate(layoutInflater, parent, false)
                return ChatFromImageViewHolder(binding)
            }
        }
    }

    class ChatToImageViewHolder private constructor(val binding: CardChatToImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Chat, profileImage: String) {
            binding.chat = item
            binding.profileImage = profileImage
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ChatToImageViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CardChatToImageBinding.inflate(layoutInflater, parent, false)
                return ChatToImageViewHolder(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.senderId == FirebaseAuth.getInstance().uid) {
            if (item.type == "text")
                1
            else
                2
        } else {
            if (item.type == "text")
                3
            else
                4
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> ChatToTextViewHolder.from(parent)
            2 -> ChatToImageViewHolder.from(parent)
            3 -> ChatFromTextViewHolder.from(parent)
            else -> ChatFromImageViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is ChatToTextViewHolder -> holder.bind(item, senderProfileImage)
            is ChatToImageViewHolder -> holder.bind(item, senderProfileImage)
            is ChatFromTextViewHolder -> holder.bind(item, receiverProfileImage)
            is ChatFromImageViewHolder -> holder.bind(item, receiverProfileImage)
        }
        //For grouping fix
//        if (position > 0) {
//            val oldViewType = getItemViewType(position - 1)
//            when (holder) {
//                is ChatToTextViewHolder -> {
//                    if (oldViewType <= 2) holder.binding.imageTo.visibility = View.INVISIBLE
//                    else
//                        holder.binding.imageTo.visibility = View.VISIBLE
//                }
//                is ChatToImageViewHolder -> {
//                    if (oldViewType <= 2) holder.binding.imageTo.visibility = View.INVISIBLE
//                    else
//                        holder.binding.imageTo.visibility = View.VISIBLE
//                }
//                is ChatFromTextViewHolder -> {
//                    if (oldViewType > 2) holder.binding.imageFrom.visibility = View.INVISIBLE
//                    else
//                        holder.binding.imageFrom.visibility = View.VISIBLE
//                }
//                is ChatFromImageViewHolder -> {
//                    if (oldViewType > 2) holder.binding.imageFrom.visibility = View.INVISIBLE
//                    else
//                        holder.binding.imageFrom.visibility = View.VISIBLE
//                }
//            }
//        } else {
//            when (holder) {
//                is ChatToTextViewHolder -> holder.binding.imageTo.visibility = View.VISIBLE
//                is ChatToImageViewHolder -> holder.binding.imageTo.visibility = View.VISIBLE
//                is ChatFromTextViewHolder -> holder.binding.imageFrom.visibility = View.VISIBLE
//                is ChatFromImageViewHolder -> holder.binding.imageFrom.visibility = View.VISIBLE
//            }
//        }
    }
}

class ChatDiffCallback : DiffUtil.ItemCallback<Chat>() {
    override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
        return oldItem.timeStamp == newItem.timeStamp
    }

    override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
        return oldItem == newItem
    }
}




