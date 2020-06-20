package com.example.pencast.ui.chat

import com.bumptech.glide.Glide
import com.example.pencast.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.card_chat_from.view.*
import kotlinx.android.synthetic.main.card_chat_to.view.*
import kotlinx.android.synthetic.main.card_user.view.*
import java.text.SimpleDateFormat
import java.util.*

class ChatFromItem(val chat: Chat) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.card_chat_from
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.message_from.text = chat.message
        Glide.with(viewHolder.itemView.image_from.context)
            .load(chat.proileImage)
            .into(viewHolder.itemView.image_from)
        val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.ENGLISH)
        val dataString = formatter.format(Date(chat.timeStamp))
        viewHolder.itemView.recieve_time.text = "At $dataString"
    }
}