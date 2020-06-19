package com.example.pencast.ui.chat

import com.bumptech.glide.Glide
import com.example.pencast.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.card_chat_from.view.*
import kotlinx.android.synthetic.main.card_chat_to.view.*
import kotlinx.android.synthetic.main.card_user.view.*

class ChatToItem(val chat: Chat) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.card_chat_to
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.message_to.text=chat.message
        Glide.with(viewHolder.itemView.image_to.context)
            .load(chat.profileImage)
            .into(viewHolder.itemView.image_to)
    }
}