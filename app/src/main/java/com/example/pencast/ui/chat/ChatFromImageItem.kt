package com.example.pencast.ui.chat

import com.bumptech.glide.Glide
import com.example.pencast.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.card_chat_from_image.view.*
import kotlinx.android.synthetic.main.card_chat_from_text.view.image_from
import kotlinx.android.synthetic.main.card_chat_from_text.view.recieve_time
import java.text.SimpleDateFormat
import java.util.*

class ChatFromImageItem(val chat: Chat, val profileImage: String) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.card_chat_from_image
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        Glide.with(viewHolder.itemView.image_message_from.context)
            .load(chat.message)
            .into(viewHolder.itemView.image_message_from)
        Glide.with(viewHolder.itemView.image_from.context)
            .load(profileImage)
            .into(viewHolder.itemView.image_from)
        val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.ENGLISH)
        val dataString = formatter.format(Date(chat.timeStamp))
        viewHolder.itemView.recieve_time.text = "At $dataString"
    }
}