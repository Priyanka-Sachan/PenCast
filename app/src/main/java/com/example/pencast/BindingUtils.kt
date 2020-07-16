package com.example.pencast

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.card_chat_list.view.*
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .into(imgView)
    }
}

@BindingAdapter("timeStamp")
fun bindText(textView: TextView, timeStamp: Long) {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
    val dataString = formatter.format(Date(timeStamp))
    textView.text = "At $dataString"
}