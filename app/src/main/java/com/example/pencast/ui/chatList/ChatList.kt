package com.example.pencast.ui.chatList

data class ChatList(
    var uid: String,
    var username: String,
    var profileImage: String,
    var message: String,
    var timeStamp: Long
) {
    constructor() : this("", "", "", "",0)
}