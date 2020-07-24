package com.example.pencast.ui.latestMessage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pencast.databinding.CardLatestMessageBinding

class LatestMessageAdapter(private val latestMessageClickListener: LatestMessageClickListener) :
    ListAdapter<LatestMessage,
            LatestMessageAdapter.ViewHolder>(LatestMessageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(latestMessageClickListener, item)
    }

    class ViewHolder private constructor(val binding: CardLatestMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(latestMessageClickListener: LatestMessageClickListener, item: LatestMessage) {
            binding.latestMessage = item
            binding.latestMessageClickListener = latestMessageClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CardLatestMessageBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class LatestMessageDiffCallback : DiffUtil.ItemCallback<LatestMessage>() {
    override fun areItemsTheSame(oldItem: LatestMessage, newItem: LatestMessage): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: LatestMessage, newItem: LatestMessage): Boolean {
        return oldItem == newItem
    }
}

class LatestMessageClickListener(val latestMessageClickListener: (latestMessage: LatestMessage) -> Unit) {
    fun onClick(latestMessage: LatestMessage) = latestMessageClickListener(latestMessage)
}


