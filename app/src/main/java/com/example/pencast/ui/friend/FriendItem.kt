package com.example.pencast.ui.friend

import com.bumptech.glide.Glide
import com.example.pencast.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.card_user.view.*

class FriendItem(val friend: Friend) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.card_user
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.user_username.text = friend.username
        viewHolder.itemView.user_status.text = "Status"
        Glide.with(viewHolder.itemView.user_profile_image.context)
            .load(friend.profileImage)
            .into(viewHolder.itemView.user_profile_image)
    }
}