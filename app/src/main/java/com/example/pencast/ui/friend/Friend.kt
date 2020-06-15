package com.example.pencast.ui.friend

data class Friend(var uid: String ,var profileImage: String,var username: String) {
    constructor():this("","","")
}
