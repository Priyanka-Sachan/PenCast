package com.example.pencast.ui.chat
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.ListAdapter
//import androidx.recyclerview.widget.RecyclerView
//import com.example.pencast.databinding.CardChatFromImageBinding
//
//class ChatAdapter() : ListAdapter<Chat,
//        ChatAdapter.ChatFromImageViewHolder>(ChatDiffCallback()) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatFromImageViewHolder {
//        return ChatFromImageViewHolder.from(parent)
//    }
//
//    override fun onBindViewHolder(holder: ChatFromImageViewHolder, position: Int) {
//        val item = getItem(position)
//        holder.bind( item)
//    }
//
//    class ChatFromImageViewHolder private constructor(val binding: CardChatFromImageBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun bind( item: Chat) {
//            binding.chat = item
//            binding.executePendingBindings()
//        }
//
//        companion object {
//            fun from(parent: ViewGroup): ChatFromImageViewHolder {
//                val layoutInflater = LayoutInflater.from(parent.context)
//                val binding = CardChatFromImageBinding.inflate(layoutInflater, parent, false)
//                return ChatFromImageViewHolder(binding)
//            }
//        }
//    }
//}
//
//class ChatDiffCallback : DiffUtil.ItemCallback<Chat>() {
//    override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
//        return oldItem.timeStamp == newItem.timeStamp
//    }
//
//    override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
//        return oldItem == newItem
//    }
//}
//
//
//
//
