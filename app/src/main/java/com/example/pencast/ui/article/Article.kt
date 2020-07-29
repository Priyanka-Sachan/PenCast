package com.example.pencast.ui.article

import com.example.pencast.login.User

data class Article(
    var title: String,
    var subTitle: String,
    var author: User,
    var details: String,
    var imageUrl: String,
    var edited: Long,
    var favouriteOf: Long
)