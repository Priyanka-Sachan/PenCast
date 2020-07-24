package com.example.pencast.ui.latestMessage

data class LatestMessage(
    var uid: String,
    var username: String,
    var profileImage: String,
    var status: String,
    var message: String,
    var timeStamp: Long
) {
    constructor() : this("", "", "", "", "", 0)
}