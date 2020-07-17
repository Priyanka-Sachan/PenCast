package com.example.pencast

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.card_chat_list.view.*
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image))
            .into(imgView)
    }
}

@BindingAdapter("timeStampShort")
fun timeStampShort(textView: TextView, timeStamp: Long) {
    val formatter = SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH)
    val dataString = formatter.format(Date(timeStamp))
    textView.text = "At $dataString"
}

@BindingAdapter("timeStampLong")
fun timeStampLong(textView: TextView, timeStamp: Long) {
    val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm", Locale.ENGLISH)
    val dataString = formatter.format(Date(timeStamp))
    textView.text = "At $dataString"
}