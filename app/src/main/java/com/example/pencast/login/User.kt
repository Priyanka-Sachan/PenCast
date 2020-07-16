package com.example.pencast.login

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var uid: String,
    var username: String,
    var profileImage: String,
    var status: String
) : Parcelable {
    constructor() : this("", "", "", "")
}