package com.example.pencast.ui.chat

import com.bumptech.glide.Glide
import com.example.pencast.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.card_chat_from_text.view.*
import java.text.SimpleDateFormat
import java.util.*

class ChatFromTextItem(val chat: Chat, val profileImage: String) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.card_chat_from_text
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.message_from.text = chat.message
        Glide.with(viewHolder.itemView.image_from.context)
            .load(profileImage)
            .into(viewHolder.itemView.image_from)
        val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.ENGLISH)
        val dataString = formatter.format(Date(chat.timeStamp))
        viewHolder.itemView.recieve_time.text = "At $dataString"
    }
}