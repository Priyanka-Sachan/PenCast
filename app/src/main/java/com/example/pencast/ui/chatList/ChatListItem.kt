package com.example.pencast.ui.chatList

import com.bumptech.glide.Glide
import com.example.pencast.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.card_chat_list.view.*
import java.text.SimpleDateFormat
import java.util.*

class ChatListItem(val chatList: ChatList) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.card_chat_list
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.chat_list_username.text = chatList.username
        viewHolder.itemView.chat_list_message.text = chatList.message
        Glide.with(viewHolder.itemView.chat_list_profile_image.context)
            .load(chatList.profileImage)
            .into(viewHolder.itemView.chat_list_profile_image)
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val dataString = formatter.format(Date(chatList.timeStamp))
        viewHolder.itemView.chat_list_timestamp.text = "At $dataString"
    }
}