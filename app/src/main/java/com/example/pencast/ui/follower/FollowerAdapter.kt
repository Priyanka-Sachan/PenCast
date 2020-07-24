package com.example.pencast.ui.follower

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pencast.databinding.CardFollowerBinding
import com.example.pencast.login.User


class FollowerAdapter(private val followerClickListener: FollowerClickListener) : ListAdapter<User,
        FollowerAdapter.ViewHolder>(FollowerDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, followerClickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: CardFollowerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: User, followerClickListener: FollowerClickListener) {
            binding.follower = item
            binding.followerClickListener = followerClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CardFollowerBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class FollowerDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}

class FollowerClickListener(val followerClickListener: (user: User) -> Unit) {
    fun onClick(user: User) = followerClickListener(user)
}
