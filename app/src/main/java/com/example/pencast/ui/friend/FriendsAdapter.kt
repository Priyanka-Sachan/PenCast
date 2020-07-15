package com.example.pencast.ui.friend

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pencast.databinding.CardUserBinding
import com.example.pencast.login.User

class FriendsAdapter(val friendClickListener: FriendClickListener) : ListAdapter<User,
        FriendsAdapter.ViewHolder>(FriendDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(friendClickListener, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: CardUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(friendClickListener: FriendClickListener, item: User) {
            binding.friend = item
            binding.friendClickListener = friendClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CardUserBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class FriendDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}

class FriendClickListener(val friendClickListener: (user: User) -> Unit) {
    fun onClick(user: User) = friendClickListener(user)
}
