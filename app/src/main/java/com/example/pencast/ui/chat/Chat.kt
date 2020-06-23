package com.example.pencast.ui.chat

data class Chat(var message: String, var senderId: String,var receiverId:String, var timeStamp: Long) {
    constructor() : this("", "","", 0)
}