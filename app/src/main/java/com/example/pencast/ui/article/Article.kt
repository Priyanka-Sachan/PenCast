package com.example.pencast.ui.article

import android.os.Parcelable
import com.example.pencast.login.User
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Article(
    var articleId: String,
    var title: String,
    var subTitle: String,
    var author: User? = null,
    var details: String,
    var imageUrl: String,
    var edited: Long,
    var favouriteOf: Long
) : Parcelable {
    constructor() : this("", "", "", null, "", "", 0, 0)
}