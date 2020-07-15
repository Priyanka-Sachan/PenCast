package com.example.pencast.ui.chat

import com.bumptech.glide.Glide
import com.example.pencast.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.card_chat_to_image.view.*
import kotlinx.android.synthetic.main.card_chat_to_text.view.image_to
import kotlinx.android.synthetic.main.card_chat_to_text.view.send_time
import java.text.SimpleDateFormat
import java.util.*

class ChatToImageItem(val chat: Chat, val profileImage: String) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.card_chat_to_image
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        Glide.with(viewHolder.itemView.image_message_to.context)
            .load(chat.message)
            .into(viewHolder.itemView.image_message_to)
        Glide.with(viewHolder.itemView.image_to.context)
            .load(profileImage)
            .into(viewHolder.itemView.image_to)
        val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.ENGLISH)
        val dataString = formatter.format(Date(chat.timeStamp))
        viewHolder.itemView.send_time.text = "At $dataString"
    }
}