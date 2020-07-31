package com.example.pencast.ui.article

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ArticleInfo(
    var articleId: String,
    var title: String,
    var imageUrl: String
) : Parcelable {
    constructor() : this("", "", "")
}