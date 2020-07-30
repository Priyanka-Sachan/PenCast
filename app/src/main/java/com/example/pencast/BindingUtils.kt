package com.example.pencast

import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.pencast.ui.article.Article
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
                    .error(R.drawable.ic_broken_image)
            )
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

@BindingAdapter("longToString")
fun longToString(textView: TextView, long: Long) {
    textView.text = long.toString()
}

@BindingAdapter("shareArticle")
fun shareArticle(button: androidx.appcompat.widget.AppCompatButton, article: Article) {
    button.setOnClickListener {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(
            Intent.EXTRA_TEXT,
            article.title + "\n" + article.subTitle + "\nBy: " + article.author!!.username + "\n" + article.details
        )
        intent.type = "text/plain"
        it.context.startActivity(intent)
    }
}

@BindingAdapter("toolbarBackground")
fun toolbarBackground(
    toolbar: com.google.android.material.appbar.CollapsingToolbarLayout,
    imgUrl: String
) {
//    var imgView:ImageView
//    imgUrl?.let {
//        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
//        Glide.with(toolbar.context)
//            .load(imgUri)
//            .apply(
//                RequestOptions()
//                    .placeholder(R.drawable.loading_animation)
//                    .error(R.drawable.ic_broken_image)
//            )
//            .into(imgView)
//    }
}